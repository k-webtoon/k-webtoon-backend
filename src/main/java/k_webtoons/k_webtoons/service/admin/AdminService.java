package k_webtoons.k_webtoons.service.admin;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.exception.WebtoonNotFoundException;
import k_webtoons.k_webtoons.model.admin.common.*;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;
import k_webtoons.k_webtoons.security.AccountStatus;
import k_webtoons.k_webtoons.security.HeaderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final WebtoonRepository webtoonRepository;
    private final WebtoonCommentRepository commentRepository;
    private final HeaderValidator headerValidator;

    // 전체 사용자 수 조회
    public long getTotalUsers() {
        return userRepository.count();
    }

    // 전체 웹툰 수 조회
    public long getTotalWebtoons() {
        return webtoonRepository.count();
    }

    // 전체 댓글 수 조회
    public long getTotalComments() {
        return commentRepository.count();
    }

    // 관리자 대시보드 요약 통계
    public DashboardSummaryDto getDashboardSummary() {
        return new DashboardSummaryDto(
                getTotalUsers(),
                getTotalWebtoons(),
                getTotalComments());
    }

    // 전체 사용자 목록 조회 (페이지네이션)
    public Page<FindAllUserByAdminDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> new FindAllUserByAdminDTO(
                        user.getIndexId(),
                        user.getUserEmail(),
                        user.getAccountStatus().name(),
                        user.getCreateDateTime()));
    }

    // 특정 사용자 상세 정보 조회
    public UserDetailByAdminDTO getUserDetails(Long indexId) {
        AppUser user = userRepository.findById(indexId)
                .orElseThrow(
                        () -> new CustomException("사용자를 찾을 수 없습니다. 받은 id : " + indexId, "USER NOT FOUND EXCEPTION"));
        return new UserDetailByAdminDTO(
                user.getIndexId(),
                user.getUserEmail(),
                user.getCreateDateTime(),
                user.getAccountStatus(),
                user.getUserAge(),
                user.getGender(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getSecurityQuestion());
    }

    // 특정 웹툰 비공개 처리 (관리자 권한 필요)
    @Transactional
    public void setWebtoonPrivate(Long webtoonId) {
        AppUser admin = headerValidator.getAuthenticatedUser();
        if (!"ADMIN".equals(admin.getRole())) {
            throw new CustomException("관리자 권한이 없습니다", "ADMIN_ACCESS_DENIED");
        }

        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new WebtoonNotFoundException("웹툰을 찾을 수 없습니다"));

        webtoon.setIsPublic(false);
    }

    // 사용자 상태별 수 조회 (전체, 활성, 정지, 탈퇴)
    public UserCountSummaryDTO getUserCountSummary() {
        long total = userRepository.count();
        long active = userRepository.countByAccountStatus(AccountStatus.ACTIVE);
        long suspended = userRepository.countByAccountStatus(AccountStatus.SUSPENDED);
        long deactivated = userRepository.countByAccountStatus(AccountStatus.DEACTIVATED);

        return new UserCountSummaryDTO(total, active, suspended, deactivated);
    }

    // 사용자 상태별 목록 조회 (status 필터, 페이지네이션)
    public Page<FindAllUserByAdminDTO> getUsersByStatus(String status, Pageable pageable) {
        if (status == null || status.equalsIgnoreCase("all") || status.isEmpty()) {
            return getAllUsers(pageable);
        }

        try {
            AccountStatus accountStatus = AccountStatus.valueOf(status.toUpperCase());
            return userRepository.findByAccountStatus(accountStatus, pageable)
                    .map(user -> new FindAllUserByAdminDTO(
                            user.getIndexId(),
                            user.getUserEmail(),
                            user.getAccountStatus().name(),
                            user.getCreateDateTime()));
        } catch (IllegalArgumentException e) {
            return getAllUsers(pageable);
        }
    }

    // 웹툰 전체 목록 조회 (공개/비공개 상태, 검색어 필터 적용)
    @Transactional(readOnly = true)
    public Page<AdminWebtoonListDto> getAllWebtoons(Boolean isPublic, String search, Pageable pageable) {
        Page<Webtoon> webtoons;

        boolean hasSearch = (search != null && !search.isBlank());

        if (hasSearch) {
            if (isPublic == null) {
                webtoons = webtoonRepository.findByTitleOrAuthorContaining(search, pageable);
            } else if (isPublic) { // 공개 웹툰
                webtoons = webtoonRepository.findPublicWebtoonsByTitleOrAuthor(search, pageable);
            } else { // 비공개 웹툰
                webtoons = webtoonRepository.findPrivateWebtoonsByTitleOrAuthor(search, pageable);
            }
        } else {
            if (isPublic == null) {
                webtoons = webtoonRepository.findAll(pageable);
            } else if (isPublic) { // 공개 웹툰
                webtoons = webtoonRepository.findPublicWebtoons(pageable);
            } else { // 비공개 웹툰
                webtoons = webtoonRepository.findPrivateWebtoons(pageable);
            }
        }

        return webtoons.map(AdminWebtoonListDto::fromEntity);
    }


    // 특정 웹툰 상세 조회
    @Transactional(readOnly = true)
    public AdminWebtoonDetailDto getWebtoonById(Long webtoonId) {
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new WebtoonNotFoundException("웹툰을 찾을 수 없습니다."));

        return AdminWebtoonDetailDto.fromEntity(webtoon);
    }

    // 특정 웹툰 공개/비공개 상태 업데이트 (관리자 권한 필요)
    @Transactional
    public void toggleWebtoonStatus(Long webtoonId) {
        AppUser admin = headerValidator.getAuthenticatedUser();
        if (!"ADMIN".equals(admin.getRole())) {
            throw new CustomException("관리자 권한이 없습니다", "ADMIN_ACCESS_DENIED");
        }

        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new WebtoonNotFoundException("웹툰을 찾을 수 없습니다."));

        // 🔥 현재 isPublic 값을 반대로 세팅
        webtoon.setIsPublic(!webtoon.getIsPublic());
    }


    // 웹툰 전체/공개/비공개 수 조회
    @Transactional(readOnly = true)
    public WebtoonCountSummaryDto getWebtoonCountSummary() {
        long total = webtoonRepository.count();
        long publicCount = webtoonRepository.countPublicWebtoons();
        long privateCount = webtoonRepository.countPrivateWebtoons();

        return new WebtoonCountSummaryDto(total, publicCount, privateCount);
    }

}
