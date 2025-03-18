package k_webtoons.k_webtoons.repository;

import k_webtoons.k_webtoons.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByUserEmail(String userEmail);

    Optional<AppUser> findByUserEmail(String userEmail);

    Optional<String> findRoleByUserEmail(String userEmail);
}
