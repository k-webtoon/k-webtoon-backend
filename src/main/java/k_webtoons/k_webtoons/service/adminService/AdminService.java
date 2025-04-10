package k_webtoons.k_webtoons.service.adminService;

import org.springframework.stereotype.Service;

import k_webtoons.k_webtoons.model.admin.DashboardSummaryDto;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final WebtoonRepository webtoonRepository;
    private final WebtoonCommentRepository commentRepository;

    public AdminService(UserRepository userRepository, WebtoonRepository webtoonRepository,
                        WebtoonCommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.webtoonRepository = webtoonRepository;
        this.commentRepository = commentRepository;
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalWebtoons() {
        return webtoonRepository.count();
    }

    public long getTotalComments() {
        return commentRepository.count();
    }

    // 🔽 추가: 대시보드 요약 DTO 통합 반환
    public DashboardSummaryDto getDashboardSummary() {
        return new DashboardSummaryDto(
                getTotalUsers(),
                getTotalWebtoons(),
                getTotalComments()
        );
    }
}
