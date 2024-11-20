package uz.backecommers.sms.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.backecommers.identety.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class Config {

    private final UserRepository userRepository;
    @Bean
    public TelegramBotConfig myTelegramBot() {
        return new TelegramBotConfig(userRepository);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(myTelegramBot());
        return botsApi;
    }
}
