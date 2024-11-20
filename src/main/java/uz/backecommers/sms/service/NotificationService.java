package uz.backecommers.sms.service;

public interface NotificationService {
    void sendPhoneNumber(String phoneNumber, String otp);
}
