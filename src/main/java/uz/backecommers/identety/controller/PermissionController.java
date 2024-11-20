package uz.backecommers.identety.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.backecommers.identety.dto.ApiResponse;
import uz.backecommers.identety.service.PermissionService;

@RestController
@RequestMapping("/user/permission")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("/add")
    public HttpEntity<ApiResponse> addPermission(@RequestParam String phoneNumber, @RequestParam String permission) {
        ApiResponse apiResponse = permissionService.setRoleToUser(phoneNumber, permission);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @PostMapping("/remove")
    public HttpEntity<ApiResponse> removePermission(@RequestParam String phoneNumber, @RequestParam String permission) {
        ApiResponse apiResponse = permissionService.removeRoleFromUser(phoneNumber, permission);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }
}
