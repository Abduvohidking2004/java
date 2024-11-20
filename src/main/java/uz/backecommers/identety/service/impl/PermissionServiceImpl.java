package uz.backecommers.identety.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.backecommers.identety.dto.ApiResponse;
import uz.backecommers.identety.entity.Permission;
import uz.backecommers.identety.entity.Users;
import uz.backecommers.identety.repository.PermissionRepository;
import uz.backecommers.identety.repository.UserRepository;
import uz.backecommers.identety.service.PermissionService;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public ApiResponse setRoleToUser(String phoneNumber, String roleName) {
        Optional<Users> optionalUser = userRepository.findByMobilePhone(phoneNumber);
        if (optionalUser.isEmpty()) {
            return new ApiResponse(false, MessageFormat.format("User not found with phone number: %s ", phoneNumber));
        }

        Users user = optionalUser.get();

        Optional<Permission> role = permissionRepository.findByName(roleName);
        if (role.isEmpty()) return new ApiResponse(false, MessageFormat.format("Couldn't find role %s", roleName));
        user.getRoles().add(role.get());
        Users save = userRepository.save(user);
        return new ApiResponse(true, "Success", save);
    }

    @Override
    public ApiResponse removeRoleFromUser(String phoneNumber, String roleName) {

        Optional<Users> optionalUser = userRepository.findByMobilePhone(phoneNumber);
        if (optionalUser.isEmpty()) {
            return new ApiResponse(false, MessageFormat.format("User not found with phone number: {0}", phoneNumber));
        }
        Users user = optionalUser.get();
        Optional<Permission> role = permissionRepository.findByName(roleName);
        if (role.isEmpty()) {
            return new ApiResponse(false, MessageFormat.format("Couldn't find role {0}", roleName));
        }
        if (!user.getRoles().contains(role.get())) {
            return new ApiResponse(false, MessageFormat.format("User does not have the role {0}", roleName));
        }
        user.getRoles().remove(role.get());
        Users savedUser = userRepository.save(user);
        return new ApiResponse(true, "Role successfully removed", savedUser);
    }
}
