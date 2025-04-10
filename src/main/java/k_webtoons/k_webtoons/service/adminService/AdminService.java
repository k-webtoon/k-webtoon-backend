package k_webtoons.k_webtoons.service.adminService;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.admin.FindAllUserByAdminDTO;
import k_webtoons.k_webtoons.model.admin.UserDetailByAdminDTO;
import k_webtoons.k_webtoons.model.auth.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import k_webtoons.k_webtoons.model.admin.DashboardSummaryDto;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final WebtoonRepository webtoonRepository;
    private final WebtoonCommentRepository commentRepository;

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








}
