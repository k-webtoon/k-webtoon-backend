package k_webtoons.k_webtoons.repository.user;

import k_webtoons.k_webtoons.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByUserEmail(String userEmail);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<AppUser> findByUserEmail(String userEmail);

    Optional<String> findRoleByUserEmail(String userEmail);

    Optional<AppUser> findByPhoneNumber(String phoneNumber);
}
