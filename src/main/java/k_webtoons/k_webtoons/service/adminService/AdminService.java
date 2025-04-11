package k_webtoons.k_webtoons.service.adminService;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.exception.WebtoonNotFoundException;
import k_webtoons.k_webtoons.model.admin.FindAllUserByAdminDTO;
import k_webtoons.k_webtoons.model.admin.UserDetailByAdminDTO;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.security.HeaderValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import k_webtoons.k_webtoons.model.admin.DashboardSummaryDto;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final WebtoonRepository webtoonRepository;
    private final WebtoonCommentRepository commentRepository;
    private final HeaderValidator headerValidator;

    // 전체 사용자 수
    public long getTotalUsers() {
        return userRepository.count();
    }

    // 전체 웹툰 수
    public long getTotalWebtoons() {
        return webtoonRepository.count();
    }

    // 전체 댓글 수
    public long getTotalComments() {
        return commentRepository.count();
    }

    // 요약
    public DashboardSummaryDto getDashboardSummary() {
        return new DashboardSummaryDto(
                getTotalUsers(),
                getTotalWebtoons(),
                getTotalComments()
        );
    }

    // 전체 사용자 찾아오기(페이지 기능)
    public Page<FindAllUserByAdminDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> new FindAllUserByAdminDTO(
                        user.getIndexId(),
                        user.getUserEmail(),
                        user.getAccountStatus().name(),
                        user.getCreateDateTime()
                ));
    }

    public UserDetailByAdminDTO getUserDetails(Long indexId) {
        AppUser user = userRepository.findById(indexId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다. 받은 id : " + indexId, "USER NOT FOUND EXCEPTION"));
        return new UserDetailByAdminDTO(
                user.getIndexId(),
                user.getUserEmail(),
                user.getCreateDateTime(),
                user.getAccountStatus(),
                user.getUserAge(),
                user.getGender(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getSecurityQuestion()
        );
    }

    //웹툰 비공개 처리

    @Transactional
    public void setWebtoonPrivate(Long webtoonId) {
        // 1. 관리자 권한 확인
        AppUser admin = headerValidator.getAuthenticatedUser();
        if (!"ADMIN".equals(admin.getRole())) {
            throw new CustomException("관리자 권한이 없습니다", "ADMIN_ACCESS_DENIED");
        }

        // 2. 웹툰 존재 여부 확인
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new WebtoonNotFoundException("웹툰을 찾을 수 없습니다"));

        // 3. 비공개 처리
        webtoon.setIsPublic(false);
    }


}
