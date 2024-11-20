package uz.backecommers.sms.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import uz.backecommers.sms.service.NotificationService;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendPhoneNumber(String phoneNumber, String otp) {
        System.out.println(otp);
        //  todo
    }
}
