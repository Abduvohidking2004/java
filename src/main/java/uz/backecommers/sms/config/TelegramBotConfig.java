package uz.backecommers.sms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.backecommers.identety.entity.Users;
import uz.backecommers.identety.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component

public class TelegramBotConfig extends TelegramLongPollingBot {

    private final UserRepository userRepository;

    @Value("${telegram.bot-token}")
    private String botToken;

    @Value("${telegram.bot-username}")
    private String botUsername;

    public TelegramBotConfig(UserRepository userRepository) {

        this.userRepository = userRepository;
    }


    @Override
    public String getBotUsername() {
        return botUsername;  // Bot username
    }

    @Override
    public String getBotToken() {
        return botToken; // Bot token from BotFather
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            System.out.println(messageText);
            if (messageText.equals("/start")) {
                sendContactRequestMessage(chatId);
            } else {
                sendTextMessage(chatId, "Siz yozdingiz: " + messageText);
            }
        }

        if (update.hasMessage() && update.getMessage().hasContact()) {
            Contact contact = update.getMessage().getContact();
            Long chatId = update.getMessage().getChatId();
            String phoneNumber = contact.getPhoneNumber();

            saveUser(chatId, phoneNumber);

            sendTextMessageWithKeyboardRemove(chatId, "Rahmat! Sizning nomeringiz saqlandi: " + phoneNumber);
        }
    }


    public void saveUser(Long chatId, String phoneNumber) {
        if (phoneNumber.startsWith("+")) {
            // "+" ni olib tashlash
            phoneNumber = phoneNumber.substring(1);
        }
        Optional<Users> byMobilePhone = userRepository.findByUsername(phoneNumber);
        if (byMobilePhone.isEmpty()) {
            sendTextMessage(chatId, "Bu telefon raqamli foydalanuvchi ro`yhatdan o`tmagan");
        }
        Users users = byMobilePhone.get();
        users.setTelegramId(chatId);

        userRepository.save(users);
    }

    public void sendContactRequestMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Assalomu Aleykum! Iltimos, menga nomeringizni taqdim eting.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton shareContactButton = new KeyboardButton();
        shareContactButton.setText("Nomerni ulashish");
        shareContactButton.setRequestContact(true); // Kontakt jo'natishni so'rash
        row.add(shareContactButton);

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendTextMessageWithKeyboardRemove(Long chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText);

        // Klaviaturani o'chirish
        ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
        keyboardRemove.setRemoveKeyboard(true);
        message.setReplyMarkup(keyboardRemove);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void sendTextMessage(Long chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
