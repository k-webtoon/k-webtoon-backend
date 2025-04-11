package k_webtoons.k_webtoons.controller.auth;

import jakarta.validation.Valid;
import k_webtoons.k_webtoons.model.auth.dto.AccountStatusRequest;
import k_webtoons.k_webtoons.service.auth.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/user_ma")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserManagementService userManagementService;

    // 계정을 활성화 상태로 변경
    @PatchMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestBody @Valid AccountStatusRequest request) {
        userManagementService.activateAccount(request);
        return ResponseEntity.ok("Account activated successfully");
    }

    // 계정을 정지 상태로 변경 (임시)
    @PatchMapping("/suspend")
    public ResponseEntity<String> suspendAccount(@RequestBody @Valid AccountStatusRequest request) {
        userManagementService.suspendAccount(request);
        return ResponseEntity.ok("Account suspended successfully");
    }

    // 계정을 비활성화 상태로 변경 (영구)
    @PatchMapping("/deactivate")
    public ResponseEntity<String> deactivateAccount(@RequestBody @Valid AccountStatusRequest request) {
        userManagementService.deactivateAccount(request);
        return ResponseEntity.ok("Account deactivated successfully");
    }
}