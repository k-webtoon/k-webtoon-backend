package k_webtoons.k_webtoons.service.user;

import k_webtoons.k_webtoons.model.user.AppUser;
import k_webtoons.k_webtoons.model.user.UserRegisterDTO;
import k_webtoons.k_webtoons.model.user.UserResponse;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(UserRegisterDTO dto) {
        if (userRepository.existsByUserEmail(dto.userEmail())) {
            throw new RuntimeException("이미 사용중인 이메일입니다.");
        }

        String role = "USER";

        AppUser newAppUser = new AppUser(
                dto.userEmail(),
                passwordEncoder.encode(dto.userPassword()),
                dto.userAge(),
                dto.gender(),
                dto.nickname(),
                role
        );

        AppUser savedAppUser = userRepository.save(newAppUser);

        return new UserResponse(
                savedAppUser.getIndexId(),
                savedAppUser.getUserEmail(),
                savedAppUser.getNickname()
        );
    }

    public String getUserRoleByEmail(String email) {
        AppUser user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없음"));
        System.out.println("role 은 : " +user.getRole());
        return user.getRole();
    }
}
