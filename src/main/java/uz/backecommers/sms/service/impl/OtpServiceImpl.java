package uz.backecommers.sms.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.backecommers.identety.dto.ApiResponse;
import uz.backecommers.sms.entity.Otp;
import uz.backecommers.sms.repository.OtpRepository;
import uz.backecommers.sms.service.NotificationService;
import uz.backecommers.sms.service.OtpService;
import uz.backecommers.sms.util.OtpGenerateUtil;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpServiceImpl implements OtpService {
    //
    OtpRepository repository;
    //
    NotificationService notificationService;
    //
    PasswordEncoder passwordEncoder;

    @Override
    public String sendPhoneNumber(String phoneNumber) {
        String otpCode = OtpGenerateUtil.generate();
        String content = "KODNI HECH KIMGA BERMANG! OTP code: {%s}.".formatted(otpCode);

        Otp otp = Otp.builder()
                .phoneNumber(phoneNumber)
                .code(passwordEncoder.encode(otpCode))
                .content(content)
                .build();
        repository.save(otp);

        notificationService.sendPhoneNumber(otp.getPhoneNumber(), otpCode);
        return otp.getId().toString();
    }

    @Override
    public ApiResponse confirm(String id, String code) {
        Optional<Otp> optionalOtp = repository.findById(UUID.fromString(id));

        if (optionalOtp.isEmpty()) {
            return new ApiResponse(false, "Refresh token is invalid or expired!");
        }

        Otp otp = optionalOtp.get();

        if (otp.getUsed()) {
            return new ApiResponse(false, "OTP already used");
        }
        if (otp.getRepeat() >= 3) {
            return new ApiResponse(false, "OTP repeat limit exceeded");
        }

        long currentTime = System.currentTimeMillis();  // Hozirgi vaqt millisekundlarda
        long otpUsedTime = otp.getCreatedDate().getTime();  // Sekundlardan millisekundlarga o'tkazish

        System.out.println("Current Time (ms): " + currentTime);
        System.out.println("OTP Used Time (ms): " + otpUsedTime);

        if (currentTime - otpUsedTime > 120000) { // 3 minutes in milliseconds
            return new ApiResponse(false, "OTP expire time exceeded");
        }


        if (!passwordEncoder.matches(code, otp.getCode())) {
            otp.setRepeat(otp.getRepeat() + 1);
            repository.save(otp);
            return new ApiResponse(false, "OTP code mismatch");
        }

        otp.setUsed(true);
        repository.save(otp);
        return new ApiResponse(true, "OTP confirmed successfully");
    }

}