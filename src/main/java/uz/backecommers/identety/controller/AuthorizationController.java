package uz.backecommers.identety.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.backecommers.identety.dto.ApiResponse;
import uz.backecommers.identety.dto.request.*;
import uz.backecommers.identety.service.AuthService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorizationController {

    AuthService service;

    @PostMapping("check")
    public ResponseEntity<ApiResponse> checkPhone(@RequestBody PhoneRequest request) {
        ApiResponse apiResponse = service.checkPhoneNumber(request);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @PostMapping("registration")
    public ResponseEntity<ApiResponse> registration(@RequestBody @Valid UserRegisterRequest request) {
        ApiResponse registration = service.registration(request);
        return ResponseEntity.status(registration.isSuccess() ? 200 : 400).body(registration);
    }

    @PostMapping("/registration/confirm")
    public ResponseEntity<ApiResponse> registrationConfirm(@RequestBody RegisterVerifyRequest request) {
        ApiResponse apiResponse = service.registrationConfirm(request);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@RequestBody UserLoginRequest request) {
        ApiResponse login = service.login(request);
        return ResponseEntity.status(login.isSuccess() ? 200 : 400).body(login);
    }

    @PostMapping("refresh_token")
    public ResponseEntity<ApiResponse> getAccessTokenByRefresh(@RequestBody RefreshTokenRequest refreshToken) {
        ApiResponse accessTokenByRefresh = service.getAccessTokenByRefresh(refreshToken);
        return ResponseEntity.status(accessTokenByRefresh.isSuccess() ? 200 : 400).body(accessTokenByRefresh);
    }


    @PostMapping("forgot_password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody @Valid PhoneRequest request) {
        ApiResponse apiResponse = service.forgotPassword(request);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @PostMapping("forgot_password/confirm")
    public ResponseEntity<ApiResponse> forgotPasswordConfirm(@RequestBody OtpVerifyRequest request) {
        ApiResponse apiResponse = service.forgotPasswordConfirm(request);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @PostMapping("change_password")
    public ResponseEntity<ApiResponse> changePassword(HttpServletRequest httpServletRequest, @RequestBody @Valid ChangePasswordRequest request) {
        ApiResponse apiResponse = service.changePassword(httpServletRequest, request);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

}