package k_webtoons.k_webtoons.initializer;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.phone}")
    private String adminPhone;

    @Value("${admin.security.question}")
    private String securityQuestion;

    @Value("${admin.security.answer}")
    private String securityAnswer;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsById(1L)) {
            AppUser adminUser = new AppUser(
                    adminEmail,                            // 이메일
                    passwordEncoder.encode(adminPassword), // 비밀번호 (암호화)
                    20,                                    // 나이 (하드코딩 가능)
                    "남자",                                // 성별 (하드코딩 가능)
                    "SuperAdmin",                          // 닉네임 (하드코딩 가능)
                    "ADMIN",                               // 역할 (어드민)
                    adminPhone,                            // 전화번호
                    securityQuestion,                      // 보안 질문
                    securityAnswer                         // 보안 답변
            );
            userRepository.save(adminUser);
            System.out.println("Admin account created: " + adminEmail + " / " + adminPassword);
        }
    }
}

