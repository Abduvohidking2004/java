package uz.backecommers.identety.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.backecommers.identety.entity.Permission;
import uz.backecommers.identety.repository.PermissionRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PermissionRepository roleRepository;

    @Override
    public void run(String... args) {
        List<String> defaultRoles = List.of("USER", "ADMIN");

        for (String roleName : defaultRoles) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                roleRepository.save(Permission.builder().name(roleName).build());
            }
        }
    }
}