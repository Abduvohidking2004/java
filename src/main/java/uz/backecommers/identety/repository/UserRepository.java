package uz.backecommers.identety.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backecommers.identety.entity.Users;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByUsernameAndState(String username, Integer state);

    Optional<Users> findByUsername(String username);

    Optional<Users> findByUsernameAndTelegramIdIsNotNull(String username);

    Optional<Users> findByMobilePhone(String phoneNumber);

    Optional<Users> findByUsernameAndState(String username, Integer state);


}
