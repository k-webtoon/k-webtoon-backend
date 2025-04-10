package k_webtoons.k_webtoons.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import k_webtoons.k_webtoons.service.adminService.AdminService;
import k_webtoons.k_webtoons.model.admin.DashboardSummaryDto;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin API", description = "관리자 전용 API 모음")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard/summary")
    @Operation(summary = "대시보드 요약 통계", description = "전체 유저/웹툰/댓글 수를 요약하여 제공합니다.")
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
//
//    @GetMapping("/users")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "유저 목록 조회", description = "관리자 권한으로 전체 유저 목록을 조회합니다.")
//    public String getAllUsers() {
//        return "모든 유저 목록";
//    }
//
//    @GetMapping("/webtoon")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "웹툰 목록 조회", description = "관리자 권한으로 전체 웹툰 목록을 조회합니다.")
//    public String getAllWebtoon() {
//        return "모든 웹툰 목록";
//    }
//
//    @PostMapping("/webtoons")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "웹툰 생성", description = "새로운 웹툰을 생성합니다.")
//    public ResponseEntity<Webtoon> createWebtoon(@RequestBody Webtoon webtoon) {
//        Webtoon createdWebtoon = adminService.createWebtoon(webtoon);
//        return ResponseEntity.ok(createdWebtoon);
//    }
//
//    @PutMapping("/webtoons/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "웹툰 수정", description = "기존 웹툰 정보를 수정합니다.")
//    public ResponseEntity<Webtoon> updateWebtoon(@PathVariable Long id, @RequestBody Webtoon webtoonDetails) {
//        Webtoon updatedWebtoon = adminService.updateWebtoon(id, webtoonDetails);
//        return ResponseEntity.ok(updatedWebtoon);
//    }
//
//    @DeleteMapping("/webtoons/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "웹툰 삭제", description = "웹툰을 삭제합니다.")
//    public ResponseEntity<Void> deleteWebtoon(@PathVariable Long id) {
//        adminService.deleteWebtoon(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/users")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        User createdUser = adminService.createUser(user);
//        return ResponseEntity.ok(createdUser);
//    }
//
//    @PutMapping("/users/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "사용자 수정", description = "기존 사용자 정보를 수정합니다.")
//    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
//        User updatedUser = adminService.updateUser(id, userDetails);
//        return ResponseEntity.ok(updatedUser);
//    }
//
//    @DeleteMapping("/users/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        adminService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/comments")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "댓글 생성", description = "새로운 댓글을 생성합니다.")
//    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
//        Comment createdComment = adminService.createComment(comment);
//        return ResponseEntity.ok(createdComment);
//    }
//
//    @PutMapping("/comments/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "댓글 수정", description = "기존 댓글 정보를 수정합니다.")
//    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment commentDetails) {
//        Comment updatedComment = adminService.updateComment(id, commentDetails);
//        return ResponseEntity.ok(updatedComment);
//    }
//
//    @DeleteMapping("/comments/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
//    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
//        adminService.deleteComment(id);
//        return ResponseEntity.noContent().build();
//    }
}
