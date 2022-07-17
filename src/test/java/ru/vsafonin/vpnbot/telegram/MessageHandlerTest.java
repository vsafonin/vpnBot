package ru.vsafonin.vpnbot.telegram;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.vsafonin.vpnbot.entity.TgUser;
import ru.vsafonin.vpnbot.entity.Uuid;
import ru.vsafonin.vpnbot.service.UserService;
import ru.vsafonin.vpnbot.service.UuidSerice;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class MessageHandlerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private MessageSource messageSource;

    @Mock
    private ResourceBundle resourceBundle;

    @Mock
    private ReplyKeyboard replyKeyboard;

    @Mock
    private UuidSerice uuidSerice;

    @Mock
    private Environment env;

    @InjectMocks
    private MessageHandler messageHandler;

    private Message message;
    private User user;
    MessageHandlerTest(){
        message = mock(Message.class);
        user = mock(User.class);
    }

    @BeforeEach
    void getChatId(){
        when(message.getChatId()).thenReturn(1L);
    }

    @BeforeEach
    void getForm(){
        when(message.getFrom()).thenReturn(user);
    }

    @BeforeEach
    void getLanguage(){
        when(user.getLanguageCode()).thenReturn("EN");
    }

    /**
     * test checks that we get "need registration" message if the user not in DB
     */
    @Test
    void test_should_get_need_register_msg(){

        when(userService.getUserByChatId(any())).thenReturn(null);

        when(messageSource.getMessage("needRegistration",null, Locale.ENGLISH)).thenReturn("test");
        when(message.getText()).thenReturn("test");
        SendMessage answerMessage = messageHandler.answerMessage(message);

        assertThat(answerMessage.getText()).isEqualTo("test");
    }

    /**
     * test check that we can activate user by Invite code
     */
    @Test
    void test_should_activate_user_in_db(){
        String inviteCode = "invite";
        TgUser tgUser = new TgUser();
        when(userService.getUserByChatId(any())).thenReturn(null,tgUser);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        when(replyKeyboard.getReplyKeyboard(Locale.ENGLISH)).thenReturn(replyKeyboardMarkup);


        when(userService.getUserByInviteCode(inviteCode)).thenReturn(tgUser);
        when(message.getText()).thenReturn(inviteCode);
        when(messageSource.getMessage("greetingMessage",null,Locale.ENGLISH)).thenReturn("test");
        when(messageSource.getMessage("helpMessage",null,Locale.ENGLISH)).thenReturn("test");
        SendMessage answerMessage = messageHandler.answerMessage(message);
        assertThat(answerMessage.getText()).isEqualTo("test\ntest");
    }
    /**
     * similar test (test_should_activate_user_in_db) - checks may be user activate by link ?start=Invite-code
     *
     */
    @Test
    void test_sould_activate_user_in_db_by_link(){
        String inviteCode = "sksaldj-skajda-sklakjs-213";
        String link = "/start " + inviteCode;
        TgUser tgUser = new TgUser();
        when(userService.getUserByChatId(any())).thenReturn(null,tgUser);
        when(replyKeyboard.getReplyKeyboard(Locale.ENGLISH)).thenReturn(new ReplyKeyboardMarkup());
        when(userService.getUserByInviteCode(inviteCode)).thenReturn(tgUser);
        when(messageSource.getMessage("greetingMessage",null,Locale.ENGLISH)).thenReturn("test");
        when(messageSource.getMessage("helpMessage",null,Locale.ENGLISH)).thenReturn("test");
        when(message.getText()).thenReturn(link);
        SendMessage answerMessage = messageHandler.answerMessage(message);
        assertThat(answerMessage.getText()).isEqualTo("test\ntest");
    }

    /**
     * test checks that we get replykeyboard
     */
    @Test
    void test_should_get_reply_keyboard(){
        String inviteCode = "invite";

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        when(replyKeyboard.getReplyKeyboard(Locale.ENGLISH)).thenReturn(replyKeyboardMarkup);

        TgUser tgUser = new TgUser();
        when(userService.getUserByChatId(any())).thenReturn(null, tgUser);
        when(userService.getUserByInviteCode(inviteCode)).thenReturn(tgUser);
        when(message.getText()).thenReturn(inviteCode);
        when(messageSource.getMessage("greetingMessage", null, Locale.ENGLISH)).thenReturn("test");

        SendMessage anSendMessage = messageHandler.answerMessage(message);
        assertThat(anSendMessage.getReplyMarkup()).isEqualTo(replyKeyboardMarkup);
    }

    /**
     * test checks that we get "unknown command" message if the user send unknown command;
     */
    @Test
    void test_should_get_unknown_command_msg(){

        when(userService.getUserByChatId(any())).thenReturn(new TgUser());
        when(messageSource.getMessage("unknownCommand",null,Locale.ENGLISH)).thenReturn("test");
        when(message.getText()).thenReturn("test");
        SendMessage answerMessage = messageHandler.answerMessage(message);

        assertThat(answerMessage.getText()).isEqualTo("test");
    }

    /**
     * test check that we can get balance by command
     */
    @Test
    void test_should_get_balance_msg(){
        String textInMessage = "Получить баланс";
        when(userService.getUserByChatId(any())).thenReturn(new TgUser());
        when(message.getText()).thenReturn(textInMessage);
        when(messageSource.getMessage("balanceMessage",null,Locale.ENGLISH)).thenReturn("test");
        when(userService.getBalance(any())).thenReturn(BigDecimal.ONE);

        SendMessage answerMessage = messageHandler.answerMessage(message);
        assertThat(answerMessage.getText()).isEqualTo("test" + 1);
    }


    /**
     * test checks that user can add new TgUser
     */
    @Test
    void test_should_generate_link_for_add_new_TgUser(){
        String testInMessage = "Добавить друга";
        when(userService.getUserByChatId(any())).thenReturn(new TgUser());
        when(message.getText()).thenReturn(testInMessage);
        when(messageSource.getMessage("addFriendMessage",null, Locale.ENGLISH)).thenReturn("");
        when(env.getProperty(any())).thenReturn("test");
        SendMessage answerMessage = messageHandler.answerMessage(message);

        verify(uuidSerice,times(1)).save(any());
        assertThat(answerMessage.getText()).startsWith("<a href=\"test://test:test/test/");
        System.out.println(answerMessage.getText());

    }

}