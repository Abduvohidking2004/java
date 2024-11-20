package uz.backecommers.identety.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import uz.backecommers.identety.dto.ApiResponse;
import uz.backecommers.identety.dto.request.*;


public interface AuthService {
    ApiResponse checkPhoneNumber(PhoneRequest request);
    ApiResponse registration(UserRegisterRequest request);

    ApiResponse registrationConfirm(RegisterVerifyRequest request);

    ApiResponse login(UserLoginRequest request);

    ApiResponse getAccessTokenByRefresh(RefreshTokenRequest refreshToken);

//    JwtResponse authenticateAndGetToken(AuthRequest authRequest);

    ApiResponse forgotPassword(PhoneRequest request);

    ApiResponse forgotPasswordConfirm(OtpVerifyRequest request);

    ApiResponse changePassword(HttpServletRequest httpServletRequest, ChangePasswordRequest request);


}
