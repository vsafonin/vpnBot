package ru.vsafonin.vpnbot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.vsafonin.vpnbot.entity.User;
import ru.vsafonin.vpnbot.exception.NeedRegistrationExp;
import ru.vsafonin.vpnbot.service.UserService;

import java.util.Locale;

@Component
@Slf4j
public class MessageHandler extends SendDocument {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    public SendMessage answerMessage(Message message) {
        Integer chatId = message.getChatId().intValue();
        String answerMessageString = new String();
        Locale userLocaleInTelegramm = new Locale(message.getFrom().getLanguageCode());
        SendMessage sendMessage = new SendMessage();
        //check registration
        try {
            checkUserRegistration(chatId);
        }
        catch (NeedRegistrationExp exp) {
            answerMessageString = messageSource.getMessage("needRegistration", null, userLocaleInTelegramm);
        }
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(answerMessageString);
        return  sendMessage;
    }

    /**
     * Checks savee or not user in db, if not it throws out NeedRegistrationExp
     * @param chatId id from telegram
     * @throws NeedRegistrationExp
     */
    private void checkUserRegistration(Integer chatId) throws NeedRegistrationExp {
        User userInDb = userService.getUserByChatId(chatId);
        if (userInDb == null) {
            log.info("User: " + chatId + " is not registered");
            throw  new NeedRegistrationExp();
        }
    }
}
