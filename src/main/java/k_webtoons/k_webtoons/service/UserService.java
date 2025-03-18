package k_webtoons.k_webtoons.service;

import k_webtoons.k_webtoons.model.AppUser;
import k_webtoons.k_webtoons.model.UserRegisterDTO;
import k_webtoons.k_webtoons.model.UserResponse;
import k_webtoons.k_webtoons.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(UserRegisterDTO dto){
        if (userRepository.existsByUserEmail(dto.userEmail())) {
            throw new RuntimeException("이미 사용중인 이메일입니다.");
        }

        AppUser newAppUser = new AppUser(
                dto.userEmail(),
                passwordEncoder.encode(dto.userPassword()),
                dto.userAge(),
                dto.gender(),
                dto.nickname()
        );

        AppUser savedAppUser = userRepository.save(newAppUser);

        return new UserResponse(
                savedAppUser.getIndexId(),
                savedAppUser.getUserEmail(),
                savedAppUser.getNickname()
        );
    }
}
