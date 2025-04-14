package k_webtoons.k_webtoons.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import k_webtoons.k_webtoons.model.admin.FindAllUserByAdminDTO;
import k_webtoons.k_webtoons.model.admin.AdminStatsParams;
import k_webtoons.k_webtoons.model.admin.UserDetailByAdminDTO;
import k_webtoons.k_webtoons.model.webtoon.dto.WebtoonStatsResponse;
import k_webtoons.k_webtoons.model.admin.AdminStatsDTO;
import k_webtoons.k_webtoons.service.adminService.AdminService;
import k_webtoons.k_webtoons.model.admin.DashboardSummaryDto;
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
@Tag(name = "Admin API", description = "관리자 전용 API 모음")
@SecurityRequirement(name = "JWT")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /* 대시보드 관련 */
    @GetMapping("/dashboard/summary")
    @Operation(summary = "대시보드 요약 통계", description = "전체 유저/웹툰/댓글 수를 요약하여 제공합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요약 데이터 조회 성공")
    })
    public ResponseEntity<DashboardSummaryDto> getDashboardSummary() {
        DashboardSummaryDto summary = adminService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/users/total")
    @Operation(summary = "전체 유저 수 조회", description = "전체 유저 수를 반환합니다.")
    public ResponseEntity<Long> getTotalUsers() {
        long totalUsers = adminService.getTotalUsers();
        return ResponseEntity.ok(totalUsers);
    }

    @GetMapping("/webtoons/total")
    @Operation(summary = "전체 웹툰 수 조회", description = "전체 웹툰 수를 반환합니다.")
    public ResponseEntity<Long> getTotalWebtoons() {
        long totalWebtoons = adminService.getTotalWebtoons();
        return ResponseEntity.ok(totalWebtoons);
    }

    @GetMapping("/comments/total")
    @Operation(summary = "전체 댓글 수 조회", description = "전체 댓글 수를 반환합니다.")
    public ResponseEntity<Long> getTotalComments() {
        long totalComments = adminService.getTotalComments();
        return ResponseEntity.ok(totalComments);
    }

    @GetMapping("/users")
    @Operation(summary = "전체 사용자 조회", description = "페이지네이션 기반 사용자 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음")
    })
    public ResponseEntity<Page<FindAllUserByAdminDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<FindAllUserByAdminDTO> users = adminService.getAllUsers(pageRequest);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "사용자 상세 조회", description = "특정 사용자의 상세 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상세 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음")
    })
    public ResponseEntity<UserDetailByAdminDTO> getUserById(@PathVariable Long id) {
        UserDetailByAdminDTO dto = adminService.getUserDetails(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/webtoons/{id}/visibility")
    @Operation(summary = "웹툰 비공개 처리", description = "특정 웹툰을 비공개 상태로 전환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비공개 처리 성공"),
            @ApiResponse(responseCode = "404", description = "웹툰을 찾을 수 없음"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음")
    })
    public ResponseEntity<Map<String, String>> setWebtoonPrivate(@PathVariable Long id) {
        adminService.setWebtoonPrivate(id);
        return ResponseEntity.ok(
                Collections.singletonMap("message", "웹툰이 성공적으로 비공개 처리되었습니다"));
    }

    /* 통계 관련 */
    @GetMapping("/stats")
    @Operation(summary = "전체 통계 조회", description = "모든 통계 데이터를 한 번에 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "통계 데이터 조회 성공"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음")
    })
    public ResponseEntity<AdminStatsDTO> getAllStats() {
        return ResponseEntity.ok(adminService.getAllStats());
    }

    @GetMapping("/stats/users")
    @Operation(summary = "사용자 통계 조회")
    public ResponseEntity<AdminStatsDTO> getUserStats(@RequestParam AdminStatsParams params) {
        return ResponseEntity.ok(adminService.getUserStats(params));
    }

    @GetMapping("/stats/webtoons")
    @Operation(summary = "웹툰 통계 조회")
    public ResponseEntity<WebtoonStatsResponse> getWebtoonStats(@ModelAttribute AdminStatsParams params) {
        return ResponseEntity.ok(adminService.getWebtoonStats(params));
    }

    // @GetMapping("/stats/authors")
    // @Operation(summary = "작가 통계 조회")
    // public ResponseEntity<AdminStatsDTO> getAuthorStats(@RequestParam StatsParams
    // params) {
    // return ResponseEntity.ok(adminService.getAuthorStats(params));
    // }

    @GetMapping("/stats/comments")
    @Operation(summary = "댓글 통계 조회")
    public ResponseEntity<AdminStatsDTO> getCommentStats(@RequestParam AdminStatsParams params) {
        return ResponseEntity.ok(adminService.getCommentStats(params));
    }
}
