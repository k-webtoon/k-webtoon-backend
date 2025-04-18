package k_webtoons.k_webtoons.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import k_webtoons.k_webtoons.model.admin.common.*;
import k_webtoons.k_webtoons.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "관리자 전용 API 모음")
@SecurityRequirement(name = "JWT")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard/summary")
    @Operation(summary = "대시보드 요약 통계", description = "전체 유저/웹툰/댓글 수를 요약하여 제공합니다.")
    public ResponseEntity<DashboardSummaryDto> getDashboardSummary() {
        return ResponseEntity.ok(adminService.getDashboardSummary());
    }

    @GetMapping("/users/total")
    @Operation(summary = "전체 유저 수 조회", description = "전체 유저 수를 반환합니다.")
    public ResponseEntity<Long> getTotalUsers() {
        return ResponseEntity.ok(adminService.getTotalUsers());
    }

    @GetMapping("/webtoons/total")
    @Operation(summary = "전체 웹툰 수 조회", description = "전체 웹툰 수를 반환합니다.")
    public ResponseEntity<Long> getTotalWebtoons() {
        return ResponseEntity.ok(adminService.getTotalWebtoons());
    }

    @GetMapping("/comments/total")
    @Operation(summary = "전체 댓글 수 조회", description = "전체 댓글 수를 반환합니다.")
    public ResponseEntity<Long> getTotalComments() {
        return ResponseEntity.ok(adminService.getTotalComments());
    }

    @GetMapping("/users")
    @Operation(summary = "전체 사용자 조회", description = "페이지네이션 기반 사용자 목록을 반환합니다.")
    public ResponseEntity<Page<FindAllUserByAdminDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.getAllUsers(pageRequest));
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "사용자 상세 조회", description = "특정 사용자의 상세 정보를 반환합니다.")
    public ResponseEntity<UserDetailByAdminDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserDetails(id));
    }

    @PutMapping("/webtoons/{id}/visibility")
    @Operation(summary = "웹툰 비공개 처리", description = "특정 웹툰을 비공개 상태로 전환합니다.")
    public ResponseEntity<Map<String, String>> setWebtoonPrivate(@PathVariable Long id) {
        adminService.setWebtoonPrivate(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "웹툰이 성공적으로 비공개 처리되었습니다"));
    }

    // 사용자 상태별 요약 통계
    @GetMapping("/users/summary")
    public ResponseEntity<UserCountSummaryDTO> getUserCountSummary() {
        return ResponseEntity.ok(adminService.getUserCountSummary());
    }

    // 상태별 사용자 리스트 (페이지네이션)
    @GetMapping("/users/by-status")
    public ResponseEntity<Page<FindAllUserByAdminDTO>> getUsersByStatus(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.getUsersByStatus(status, pageRequest));
    }
//소원 추가


    // 웹툰 목록 조회 (페이지네이션 + 검색 + 상태 필터)
    @GetMapping("/webtoons")
    @Operation(summary = "웹툰 목록 조회", description = "제목, 작가명으로 검색하거나 공개/비공개 필터링하여 웹툰 목록을 조회합니다.")
    public ResponseEntity<Page<AdminWebtoonListDto>> getAllWebtoons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean isPublic,
            @RequestParam(required = false) String search
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.getAllWebtoons(isPublic, search, pageRequest)); // 🔥 이름 맞춰서 호출
    }


    // 웹툰 상세 조회
    @GetMapping("/webtoons/{webtoonId}")
    @Operation(summary = "웹툰 상세 조회", description = "특정 웹툰의 상세 정보를 조회합니다.")
    public ResponseEntity<k_webtoons.k_webtoons.model.admin.common.AdminWebtoonDetailDto> getWebtoonById(@PathVariable Long webtoonId) {
        return ResponseEntity.ok(adminService.getWebtoonById(webtoonId));
    }

    // 웹툰 공개/비공개 상태 수정
    @PatchMapping("/webtoons/{webtoonId}/status")
    @Operation(summary = "웹툰 공개/비공개 상태 토글", description = "특정 웹툰의 공개 여부를 반전(토글)합니다.")
    public ResponseEntity<Map<String, String>> toggleWebtoonStatus(@PathVariable Long webtoonId) {
        adminService.toggleWebtoonStatus(webtoonId);
        return ResponseEntity.ok(Collections.singletonMap("message", "웹툰 공개 상태가 토글되었습니다"));
    }

    @GetMapping("/webtoons/count-summary")
    public WebtoonCountSummaryDto getWebtoonCountSummary() {
        return adminService.getWebtoonCountSummary();
    }

}
