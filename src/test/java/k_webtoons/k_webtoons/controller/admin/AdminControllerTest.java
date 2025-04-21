package k_webtoons.k_webtoons.controller.admin;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.admin.common.*;
import k_webtoons.k_webtoons.service.admin.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock private AdminService adminService;
    @InjectMocks private AdminController adminController;

    private final PageRequest pageRequest = PageRequest.of(0, 10);

    @Test
    @DisplayName("대시보드 요약 정보 조회 성공")
    void getDashboardSummary_성공() {
        // Given
        DashboardSummaryDto mockSummary = new DashboardSummaryDto(100L, 500L, 3000L);
        when(adminService.getDashboardSummary()).thenReturn(mockSummary);

        // When
        ResponseEntity<DashboardSummaryDto> response = adminController.getDashboardSummary();

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(100L, response.getBody().totalUsers());
    }

    @Test
    @DisplayName("전체 사용자 목록 조회(페이지네이션) 성공")
    void getAllUsers_페이지네이션_성공() {
        // Given
        List<FindAllUserByAdminDTO> users = List.of(
                new FindAllUserByAdminDTO(1L, "user1@test.com", "ACTIVE", null),
                new FindAllUserByAdminDTO(2L, "user2@test.com", "SUSPENDED", null)
        );
        Page<FindAllUserByAdminDTO> page = new PageImpl<>(users);
        when(adminService.getAllUsers(any())).thenReturn(page);

        // When
        ResponseEntity<Page<FindAllUserByAdminDTO>> response = adminController.getAllUsers(0, 10);

        // Then
        assertEquals(2, response.getBody().getContent().size());
        verify(adminService).getAllUsers(pageRequest);
    }

    @Test
    @DisplayName("웹툰 비공개 처리 성공")
    void setWebtoonPrivate_성공() {
        // When
        ResponseEntity<Map<String, String>> response = adminController.setWebtoonPrivate(1L);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("웹툰이 성공적으로 비공개 처리되었습니다", response.getBody().get("message"));
        verify(adminService).setWebtoonPrivate(1L);
    }

    @Test
    @DisplayName("사용자 상세 조회 실패(존재하지 않는 사용자)")
    void getUserById_존재하지않음_실패() {
        // Given
        when(adminService.getUserDetails(999L))
                .thenThrow(new CustomException("사용자를 찾을 수 없습니다", "USER_NOT_FOUND"));

        // When & Then
        CustomException ex = assertThrows(CustomException.class,
                () -> adminController.getUserById(999L));
        assertEquals("USER_NOT_FOUND", ex.getErrorCode());
    }

    @Test
    @DisplayName("웹툰 목록 조회(검색+필터) 성공 - AdminWebtoonListDto 11개 필드 검증")
    void getAllWebtoons_검색및필터_성공() {
        // Given
        AdminWebtoonListDto webtoon = new AdminWebtoonListDto(
                1L,
                "웹툰1",
                "작가1",
                "액션",
                true,
                "https://example.com/thumb.jpg",
                "테스트 시놉시스",
                List.of("태그1", "태그2"),
                1000.0,
                500.0,
                50.0
        );
        Page<AdminWebtoonListDto> page = new PageImpl<>(List.of(webtoon));
        when(adminService.getAllWebtoons(true, "검색어", pageRequest)).thenReturn(page);

        // When
        ResponseEntity<Page<AdminWebtoonListDto>> response =
                adminController.getAllWebtoons(0, 10, true, "검색어");

        // Then
        assertEquals(1, response.getBody().getContent().size());
        AdminWebtoonListDto dto = response.getBody().getContent().get(0);
        assertAll(
                () -> assertEquals(1L, dto.id()),
                () -> assertEquals("웹툰1", dto.titleName()),
                () -> assertEquals("작가1", dto.author()),
                () -> assertEquals("액션", dto.genre()),
                () -> assertTrue(dto.isPublic()),
                () -> assertEquals("https://example.com/thumb.jpg", dto.thumbnailUrl()),
                () -> assertEquals("테스트 시놉시스", dto.synopsis()),
                () -> assertEquals(List.of("태그1", "태그2"), dto.tags()),
                () -> assertEquals(1000.0, dto.totalCount()),
                () -> assertEquals(500.0, dto.favoriteCount()),
                () -> assertEquals(50.0, dto.collectedNumOfEpi())
        );
    }

    @Test
    @DisplayName("웹툰 상태 토글 실패(관리자 권한 없음)")
    void toggleWebtoonStatus_권한없음_실패() {
        // Given
        doThrow(new CustomException("관리자 권한이 없습니다", "ADMIN_ACCESS_DENIED"))
                .when(adminService).toggleWebtoonStatus(1L);

        // When & Then
        CustomException ex = assertThrows(CustomException.class,
                () -> adminController.toggleWebtoonStatus(1L));
        assertEquals("ADMIN_ACCESS_DENIED", ex.getErrorCode());
    }

    @Test
    @DisplayName("웹툰 수 통계 조회 성공")
    void getWebtoonCountSummary_성공() {
        // Given
        WebtoonCountSummaryDto dto = new WebtoonCountSummaryDto(100L, 80L, 20L);
        when(adminService.getWebtoonCountSummary()).thenReturn(dto);

        // When
        WebtoonCountSummaryDto result = adminController.getWebtoonCountSummary();

        // Then
        assertEquals(100L, result.totalWebtoons());
        assertEquals(20L, result.privateWebtoons());
    }
}
