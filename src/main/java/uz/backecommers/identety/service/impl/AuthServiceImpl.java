package uz.backecommers.identety.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.backecommers.identety.dto.ApiResponse;
import uz.backecommers.identety.dto.request.*;
import uz.backecommers.identety.dto.response.CheckPhoneResponse;
import uz.backecommers.identety.dto.response.JwtResponse;
import uz.backecommers.identety.dto.response.UserRegistrationResponse;
import uz.backecommers.identety.entity.Permission;
import uz.backecommers.identety.entity.RefreshToken;
import uz.backecommers.identety.entity.Users;
import uz.backecommers.identety.repository.PermissionRepository;
import uz.backecommers.identety.repository.UserRepository;
import uz.backecommers.identety.service.AuthService;
import uz.backecommers.identety.service.JwtService;
import uz.backecommers.identety.service.RefreshTokenService;
import uz.backecommers.sms.service.OtpService;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@RequiredArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    //
    UserRepository repository;
    //
    PasswordEncoder passwordEncoder;
    OtpService otpService;
    JwtService jwtService;
    RefreshTokenService refreshTokenService;
    PermissionRepository permissionRepository;


    @Override
    public ApiResponse checkPhoneNumber(PhoneRequest request) {

        log.info(String.format("check phone number request =>%s", request));

        boolean valid = repository.findByMobilePhone(request.getPhoneNumber()).isEmpty();

        return new ApiResponse(true, "success", CheckPhoneResponse.builder().isPresent(!valid).build());
    }

    @Override
    public ApiResponse registration(UserRegisterRequest request) {
        Users user = repository.findByMobilePhone(request.getPhoneNumber())
                .orElseGet(() -> {
                    Permission defaultRole = permissionRepository.findByName("USER")
                            .orElseThrow(() -> new IllegalStateException("Default role USER not found"));
                    return repository.save(Users.builder()
                            .username(request.getPhoneNumber())
                            .fullName(request.getName())
                            .mobilePhone(request.getPhoneNumber())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .roles(Collections.singleton(defaultRole)) // Set<Role>
                            .build());
                });
                    if (user.getIs_register()) {
                        return new ApiResponse(false, MessageFormat.format("User with phone number ...{0} already exist", request.getPhoneNumber()));
                    }
                    String otpId = otpService.sendPhoneNumber(request.getPhoneNumber());

                    return new ApiResponse(true, "success", UserRegistrationResponse.builder()
                            .otpId(otpId)
                            .build());
                }

        @Override
        public ApiResponse registrationConfirm (RegisterVerifyRequest request){
            Optional<Users> byMobilePhone = repository.findByMobilePhone(request.getPhoneNumber());
            if (byMobilePhone.isEmpty())
                return new ApiResponse(false, MessageFormat.format("User with phone number not found!{0}", request.getPhoneNumber()));

            Users user = byMobilePhone.get();
            // check otp
            ApiResponse confirm = otpService.confirm(request.getOtpId(), request.getOtpCode());
            if (!confirm.isSuccess()) {
                return confirm;
            }

            user.setIs_register(true);
            Users save = repository.save(user);

            return new ApiResponse(true, "success", save);
        }

        @Override
        public ApiResponse login (UserLoginRequest request){
            Optional<Users> byMobilePhone = repository.findByMobilePhone(request.getPhoneNumber());
            if (byMobilePhone.isEmpty())
                return new ApiResponse(false, format("User with phone number ...{0} not found!", request.getPhoneNumber()));
            Users user = byMobilePhone.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return new ApiResponse(false, "Invalid credentials!");
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
            return new ApiResponse(true, "success", JwtResponse.builder()
                    .accessToken(jwtService.generateToken(user.getUsername()))
                    .refreshToken(refreshToken.getRefreshToken()).build());
        }

        @Override
        public ApiResponse getAccessTokenByRefresh (RefreshTokenRequest request){
            Optional<JwtResponse> jwtResponse = refreshTokenService.findByToken(request.getRefreshToken())
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(userInfo -> {
                        String accessToken = jwtService.generateToken(userInfo.getUsername());
                        return JwtResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(request.getRefreshToken())
                                .build();
                    });
            return jwtResponse.map(response -> new ApiResponse(true, "success", response)).orElseGet(() -> new ApiResponse(false, "Refresh token is invalid or expired!"));
        }

        @Override
        public ApiResponse forgotPassword (PhoneRequest request){
            Optional<Users> byUsername = repository.findByUsername(request.getPhoneNumber());
            if (byUsername.isEmpty()) {
                return new ApiResponse(false, MessageFormat.format("User with phone ...{0} not found!", request.getPhoneNumber()));
            }
            Users user = byUsername.get();
            String otpId = otpService.sendPhoneNumber(user.getMobilePhone());
            repository.save(user);
            return new ApiResponse(true, "success", otpId);
        }

        @Override
        public ApiResponse forgotPasswordConfirm (OtpVerifyRequest request){

            if (!request.getPassword().equals(request.getConfirm_password()))
                return new ApiResponse(false, "password not equal");

            Users user = repository.findByMobilePhone(request.getPhoneNumber())
                    .orElseThrow(() -> new BadCredentialsException(MessageFormat.format("User with login...{0} not found!", request.getPhoneNumber())));

            ApiResponse confirm = otpService.confirm(request.getOtpId(), request.getOtpCode());
            if (!confirm.isSuccess()) return confirm;

            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setUpdatedAt(LocalDateTime.now().plusMinutes(5));

            repository.save(user);
            return new ApiResponse(true, "success");
        }


        @Override
        public ApiResponse changePassword (HttpServletRequest httpServletRequest, ChangePasswordRequest request){
            String authorization = httpServletRequest.getHeader("Authorization");
            Optional<Users> byMobilePhone = repository.findByMobilePhone(request.getPhoneNumber());
            if (byMobilePhone.isEmpty())
                return new ApiResponse(false, MessageFormat.format("User with phone ...{0} not found!", request.getPassword()));
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return new ApiResponse(false, MessageFormat.format("Invalid authorization header: {0}", authorization));
            }
            Users user = byMobilePhone.get();
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            repository.save(user);
            return new ApiResponse(true, "success", user.getId());
        }
    }