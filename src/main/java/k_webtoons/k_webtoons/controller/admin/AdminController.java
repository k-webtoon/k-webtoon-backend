package k_webtoons.k_webtoons.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import k_webtoons.k_webtoons.model.admin.*;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonStatsResponse;
import k_webtoons.k_webtoons.service.adminService.AdminService;
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

}
