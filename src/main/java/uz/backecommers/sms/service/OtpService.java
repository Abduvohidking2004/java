package uz.backecommers.sms.service;

import uz.backecommers.identety.dto.ApiResponse;

public interface OtpService {
    String sendPhoneNumber(String phoneNumber);

    ApiResponse confirm(String id, String code);
}
