package ru.vsafonin.vpnbot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.vsafonin.vpnbot.entity.TgUser;
import ru.vsafonin.vpnbot.entity.Uuid;
import ru.vsafonin.vpnbot.enums.CommandType;
import ru.vsafonin.vpnbot.exception.CommandNotFoundExp;
import ru.vsafonin.vpnbot.exception.NeedRegistrationExp;
import ru.vsafonin.vpnbot.exception.UnknownInviteCodeExp;
import ru.vsafonin.vpnbot.service.UserService;
import ru.vsafonin.vpnbot.service.UuidSerice;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

@Component
@Slf4j
public class MessageHandler extends SendDocument {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    @Autowired
    private UuidSerice uuidSerice;

    @Autowired
    private ReplyKeyboard replyKeyboard;

    @Autowired
    private Environment env;

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
        StringBuilder answerMessageString = new StringBuilder();
        Locale userLocaleInTelegramm = new Locale(message.getFrom().getLanguageCode());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        //check registration
        try {
            checkUserRegistration(chatId);
        }
        catch (NeedRegistrationExp exp) {
            //check maybe user sent invite code?
            try {
                //if message starts with /start CODE - may be this is is invite code
                if (message.getText().toLowerCase().startsWith("/start")) {
                    String possibleInviteCode = message.getText().split(" ")[1];
                    activateUserByInviteCode(possibleInviteCode,chatId);
                }
                else {
                    activateUserByInviteCode(message.getText(), chatId);
                }
                log.info(">>>>> new user is registered " + chatId);
                answerMessageString.append(messageSource.getMessage("greetingMessage",null, userLocaleInTelegramm))
                                .append("\n");
                answerMessageString.append( messageSource.getMessage("helpMessage", null, userLocaleInTelegramm));
                sendMessage.setReplyMarkup(setKeyBoard(chatId,userLocaleInTelegramm)); //we must set keyboard otherwise user can not use commands
            }
            catch (UnknownInviteCodeExp unknownInviteCodeExp){
                log.info(">>>> Someone who isn't registered sent message, and this isn't invite code: " + chatId);
                answerMessageString.append(messageSource.getMessage("needRegistration", null, userLocaleInTelegramm));
            }
            sendMessage.setText(answerMessageString.toString());
            return sendMessage;

         }

        try {
            CommandType userCommand = getComamndType(message.getText(),userLocaleInTelegramm);
            return getAnswerMessage(userCommand, sendMessage, userLocaleInTelegramm,chatId);
        }
        catch (CommandNotFoundExp exp){
            log.info(">>>> user " + chatId + " write command which is not command, he write is " + message.getText());
            answerMessageString.append(messageSource.getMessage("unknownCommand", null, userLocaleInTelegramm));
            sendMessage.setReplyMarkup(setKeyBoard(chatId,userLocaleInTelegramm)); //we must set keyboard otherwise user can not use commands
            sendMessage.setText(answerMessageString.toString());
            return sendMessage;
        }


    }

    private ReplyKeyboardMarkup setKeyBoard(Integer chatId, Locale userLocaleInTelegramm) {
        TgUser tgUser = userService.getUserByChatId(chatId);
        if (tgUser.isAdmin()) {
            return  replyKeyboard.getReplyKeyboardAdmin(userLocaleInTelegramm);
        }
        else {
            return  replyKeyboard.getReplyKeyboard(userLocaleInTelegramm);
        }

    }

    /*
     * finds in DB user by InviteCode, if found set field isActive otherwise throws UnknownInviteCodeExp
     * @param inviteCode
     * @throws UnknownInviteCodeExp
     */
    private void activateUserByInviteCode(String inviteCode,Integer chatId) throws UnknownInviteCodeExp{
        TgUser tgUser = userService.getUserByInviteCode(inviteCode);
        if (tgUser == null) {
            throw new UnknownInviteCodeExp();
        }
        if (!tgUser.isActive()){
            tgUser.setChatId(chatId);
            tgUser.setActive(true);
            tgUser.setBalance(BigDecimal.ZERO);
            userService.save(tgUser);
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
        } else if (userCommand == CommandType.ADD_FRIEND) {
            answerMessage = addFriend(userLocale,chatId);
        }
        sendMessage.setText(answerMessage);
        sendMessage.setParseMode("HTML");
        return sendMessage;
    }
    /*
    this method generating new UUID code for create new TgUser
    this UUID we use for access to create user.
     */
    private String addFriend(Locale userLocale, Integer chatId) {
        TgUser tgUserInDb = userService.getUserByChatId(chatId);
        if (tgUserInDb == null) {
            throw new RuntimeException(">>> there  cannot be tgUserInDb is null in this place");
        }
        Uuid uuid = new Uuid();
        uuid.setUuid(UUID.randomUUID().toString());
        uuid.setTgUser(tgUserInDb);
        uuidSerice.save(uuid);
        String answerMessage = messageSource.getMessage("addFriendMessage", null, userLocale);

        StringBuilder urlStringBuilder = new StringBuilder();
        urlStringBuilder
                .append("<a href=\"")
                .append(env.getProperty("protocolName"))
                .append("://")
                .append(env.getProperty("domaineName"))
                .append(":")
                .append(env.getProperty("port"))
                .append("/")
                .append(env.getProperty("pathCreateNewTgUser"))
                .append("/")
                .append(uuid.getUuid())
                .append("\"> link </a>");


        return answerMessage + urlStringBuilder.toString();
    }

    private String getBalance(Locale userLocale, Integer chatId) {
        String answerMessage = messageSource.getMessage("balanceMessage",null,userLocale) +
                userService.getBalance(chatId);
        return answerMessage;
    }

    private CommandType getComamndType(String text, Locale userLocale) throws CommandNotFoundExp{
       if (text.equals(CommandType.GET_BALANCE.getCommandName(userLocale))){
            return CommandType.GET_BALANCE;
       } else if (text.equals(CommandType.ADD_FRIEND.getCommandName(userLocale))) {
           return CommandType.ADD_FRIEND;
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
