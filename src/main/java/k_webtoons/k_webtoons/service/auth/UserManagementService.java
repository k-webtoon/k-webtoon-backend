package k_webtoons.k_webtoons.service.auth;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.auth.dto.AccountStatusRequest;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.security.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserRepository userRepository;

    public void activateAccount(AccountStatusRequest request) {
        AppUser user = userRepository.findByUserEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 어드민 계정은 항상 활성화 상태 유지
        if (!user.getRole().equals("ADMIN")) {
            user.setAccountStatus(AccountStatus.ACTIVE);
        }
        userRepository.save(user);
    }

    public void suspendAccount(AccountStatusRequest request) {
        AppUser user = userRepository.findByUserEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 어드민 계정 정지 방지
        if (user.getRole().equals("ADMIN")) {
            throw new IllegalArgumentException("관리자 계정은 정지할 수 없습니다");
        }

        user.setAccountStatus(AccountStatus.SUSPENDED);
        user.setSuspendedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void deactivateAccount(AccountStatusRequest request) {
        AppUser user = userRepository.findByUserEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 어드민 계정 비활성화 방지
        if (user.getRole().equals("ADMIN")) {
            throw new IllegalArgumentException("관리자 계정은 비활성화할 수 없습니다");
        }

        user.setAccountStatus(AccountStatus.DEACTIVATED);
        userRepository.save(user);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void autoReleaseSuspendedAccounts() {
        List<AppUser> suspendedUsers = userRepository.findByAccountStatus(AccountStatus.SUSPENDED);

        suspendedUsers.forEach(user -> {
            // 어드민 계정은 처리에서 제외
            if (!user.getRole().equals("ADMIN")) {
                LocalDateTime releaseTime = user.getSuspendedAt().plusHours(24);
                if (LocalDateTime.now().isAfter(releaseTime)) {
                    user.setAccountStatus(AccountStatus.ACTIVE);
                    userRepository.save(user);
                }
            }
        });
    }
}
