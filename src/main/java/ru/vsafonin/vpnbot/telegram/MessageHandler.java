package ru.vsafonin.vpnbot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.vsafonin.vpnbot.entity.TgUser;
import ru.vsafonin.vpnbot.enums.CommandType;
import ru.vsafonin.vpnbot.exception.CommandNotFoundExp;
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

    /**
     * handles message from user
     * if the user not in db, send message "need register"
     * if the user write message and this message no in CommandType enum,
     * we send message "I don't know what is this...."
     * @param message from telegram
     * @return message to telegram
     */
    public SendMessage answerMessage(Message message) {
        Integer chatId = message.getChatId().intValue();
        String answerMessageString = new String();
        Locale userLocaleInTelegramm = new Locale(message.getFrom().getLanguageCode());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        //check registration
        try {
            checkUserRegistration(chatId);
        }
        catch (NeedRegistrationExp exp) {
            answerMessageString = messageSource.getMessage("needRegistration", null, userLocaleInTelegramm);
            sendMessage.setText(answerMessageString);
            return sendMessage;
         }

        try {
            CommandType userCommand = getComamndType(message.getText(),userLocaleInTelegramm);
            return getAnswerMessage(userCommand, sendMessage, userLocaleInTelegramm,chatId);
        }
        catch (CommandNotFoundExp exp){
            log.info(">>>> user " + chatId + " write command which is not command, he write is " + message.getText());
            answerMessageString = messageSource.getMessage("unknownCommand", null, userLocaleInTelegramm);
            sendMessage.setText(answerMessageString);
            return sendMessage;
        }


    }

    /**
     * this method calls other depending on CommandType. The other method returns String
     * and this string we are set into Sendmessage.
     * @param userCommand
     * @param sendMessage
     * @return
     */
    private SendMessage getAnswerMessage(CommandType userCommand, SendMessage sendMessage, Locale userLocale, Integer chatId) {
        String answerMessage = new String();
        if (userCommand == CommandType.GET_BALANCE){
            answerMessage = getBalance(userLocale, chatId);
        }
        sendMessage.setText(answerMessage);
        return sendMessage;
    }

    private String getBalance(Locale userLocale, Integer chatId) {
        String answerMessage = messageSource.getMessage("balanceMessage",null,userLocale) +
                userService.getBalance(chatId).toString();
        return answerMessage;
    }

    private CommandType getComamndType(String text, Locale userLocale) throws CommandNotFoundExp{
       if (text.equals(CommandType.GET_BALANCE.getCommandName(userLocale))){
            return CommandType.GET_BALANCE;
       }

       throw new CommandNotFoundExp();
    }

    /**
     * Checks saved or not user in db, if not it throws out NeedRegistrationExp
     * @param chatId id from telegram
     * @throws NeedRegistrationExp
     */
    private void checkUserRegistration(Integer chatId) throws NeedRegistrationExp {
        TgUser tgUserInDb = userService.getUserByChatId(chatId);
        if (tgUserInDb == null) {
            log.info("User: " + chatId + " is not registered");
            throw  new NeedRegistrationExp();
        }
    }
}
