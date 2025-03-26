package k_webtoons.k_webtoons.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin API", description = "관리자 전용 API 모음") // 🔹 전체 컨트롤러 설명
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "관리자 대시보드", description = "관리자 전용 대시보드 페이지에 접근합니다.")
    public String getAdminDashboard() {
        return "관리자 대시보드 접근 성공";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "유저 목록 조회", description = "관리자 권한으로 전체 유저 목록을 조회합니다.")
    public String getAllUsers() {
        return "모든 유저 목록";
    }

    @GetMapping("/webtoon")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "웹툰 목록 조회", description = "관리자 권한으로 전체 웹툰 목록을 조회합니다.")
    public String getAllWebtoon() {
        return "모든 웹툰 목록";
    }
}
