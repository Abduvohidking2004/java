package uz.backecommers.identety.service;

import uz.backecommers.identety.dto.ApiResponse;
import uz.backecommers.identety.entity.Users;

public interface PermissionService {
    ApiResponse  setRoleToUser(String phoneNumber, String roleName);
    ApiResponse  removeRoleFromUser(String phoneNumber,String roleName);
}
