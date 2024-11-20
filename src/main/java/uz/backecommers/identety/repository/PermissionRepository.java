package uz.backecommers.identety.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.backecommers.identety.entity.Permission;

import java.util.Optional;


public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
}
