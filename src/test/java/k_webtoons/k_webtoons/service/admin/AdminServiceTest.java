package k_webtoons.k_webtoons.service.admin;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.exception.WebtoonNotFoundException;
import k_webtoons.k_webtoons.model.admin.common.*;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;
import k_webtoons.k_webtoons.security.AccountStatus;
import k_webtoons.k_webtoons.security.HeaderValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private WebtoonRepository webtoonRepository;
    @Mock private HeaderValidator headerValidator;
    @Mock private WebtoonCommentRepository commentRepository;

    @InjectMocks private AdminService adminService;

    private final Long testUserId = 1L;
    private final Long testWebtoonId = 1L;
    private final Pageable pageable = PageRequest.of(0, 10);

    @Test
    @DisplayName("대시보드 통계 조회 - 전체 사용자/웹툰/댓글 수 집계")
    void getDashboardSummary_통계집계() {
        // Given
        when(userRepository.count()).thenReturn(100L);
        when(webtoonRepository.count()).thenReturn(50L);
        when(commentRepository.count()).thenReturn(1000L);

        // When
        DashboardSummaryDto result = adminService.getDashboardSummary();

        // Then
        assertAll(
                () -> assertEquals(100L, result.totalUsers()),
                () -> assertEquals(50L, result.totalWebtoons()),
                () -> assertEquals(1000L, result.totalComments())
        );
    }

    @Test
    @DisplayName("사용자 목록 조회 - 페이지네이션 적용 및 DTO 매핑 검증")
    void getAllUsers_페이지네이션_및_DTO_변환() {
        // Given
        AppUser user = new AppUser();
        user.setIndexId(testUserId);
        user.setUserEmail("test@example.com");
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setCreateDateTime(LocalDateTime.now());

        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(user)));

        // When
        Page<FindAllUserByAdminDTO> result = adminService.getAllUsers(pageable);

        // Then
        FindAllUserByAdminDTO dto = result.getContent().get(0);
        assertAll(
                () -> assertEquals(testUserId, dto.indexId()),
                () -> assertEquals("test@example.com", dto.userEmail()),
                () -> assertEquals("ACTIVE", dto.accountStatus())
        );
    }

    @Test
    @DisplayName("사용자 상세 조회 - 존재하지 않는 ID 예외 발생")
    void getUserDetails_사용자없음_예외() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        // When & Then
        CustomException ex = assertThrows(CustomException.class,
                () -> adminService.getUserDetails(testUserId));
        assertEquals("USER NOT FOUND EXCEPTION", ex.getErrorCode());
    }

    @Test
    @DisplayName("웹툰 비공개 처리 - 관리자 권한 없을 때 예외 발생")
    void setWebtoonPrivate_권한없음_예외() {
        // Given
        AppUser user = new AppUser();
        user.setRole("USER");
        when(headerValidator.getAuthenticatedUser()).thenReturn(user);

        // When & Then
        CustomException ex = assertThrows(CustomException.class,
                () -> adminService.setWebtoonPrivate(testWebtoonId));
        assertEquals("ADMIN_ACCESS_DENIED", ex.getErrorCode());
    }

    @Test
    @DisplayName("사용자 상태별 통계 - 활성/정지/탈퇴 사용자 수 집계")
    void getUserCountSummary_상태별_집계() {
        // Given
        when(userRepository.count()).thenReturn(100L);
        when(userRepository.countByAccountStatus(AccountStatus.ACTIVE)).thenReturn(70L);
        when(userRepository.countByAccountStatus(AccountStatus.SUSPENDED)).thenReturn(20L);
        when(userRepository.countByAccountStatus(AccountStatus.DEACTIVATED)).thenReturn(10L);

        // When
        UserCountSummaryDTO result = adminService.getUserCountSummary();

        // Then
        assertAll(
                () -> assertEquals(100L, result.total()),
                () -> assertEquals(70L, result.active()),
                () -> assertEquals(20L, result.suspended()),
                () -> assertEquals(10L, result.deactivated())
        );
    }

    @Test
    @DisplayName("웹툰 목록 조회 - 검색어와 공개 상태 필터 적용")
    void getAllWebtoons_검색어_및_필터() {
        // Given
        Webtoon webtoon = new Webtoon();
        webtoon.setTitleName("검색어 포함 제목");
        webtoon.setTags(new ArrayList<>());

        // 🔥 null 발생 방지를 위한 기본값 설정
        webtoon.setTotalCount(0.0);
        webtoon.setFavoriteCount(0.0);
        webtoon.setCollectedNumOfEpi(0.0);

        when(webtoonRepository.findPublicWebtoonsByTitleOrAuthor(anyString(), any()))
                .thenReturn(new PageImpl<>(List.of(webtoon)));

        // When
        Page<AdminWebtoonListDto> result =
                adminService.getAllWebtoons(true, "검색어", pageable);

        // Then
        AdminWebtoonListDto dto = result.getContent().get(0);
        assertAll(
                () -> assertEquals("검색어 포함 제목", dto.titleName()),
                () -> assertEquals(0, dto.tags().size()) // tags 검증
        );
    }
}
