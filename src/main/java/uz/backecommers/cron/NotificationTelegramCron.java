package uz.backecommers.cron;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import uz.backecommers.identety.entity.Users;
import uz.backecommers.identety.repository.UserRepository;
import uz.backecommers.sms.config.TelegramBotConfig;
import uz.backecommers.sms.entity.Otp;
import uz.backecommers.sms.repository.OtpRepository;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class NotificationTelegramCron {

    private final OtpRepository repository;
    private final TelegramBotConfig telegramBotConfig;
    private final UserRepository userRepository;


    @Scheduled(cron = "0 * * * * *")
    void senMessage(){
        List<Otp> topBySend = repository.getTopBySend(false);
        for (Otp otp : topBySend) {
            Optional<Users> byUsername = userRepository.findByUsernameAndTelegramIdIsNotNull(otp.getPhoneNumber());
            if (byUsername.isPresent()) {
                telegramBotConfig.sendTextMessage(byUsername.get().getTelegramId(), otp.getContent());

                otp.setSend(true);
                repository.save(otp);
            }

        }
    }

}
