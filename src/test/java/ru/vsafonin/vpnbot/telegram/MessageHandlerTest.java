package ru.vsafonin.vpnbot.telegram;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.vsafonin.vpnbot.entity.TgUser;
import ru.vsafonin.vpnbot.service.UserService;

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

        SendMessage answerMessage = messageHandler.answerMessage(message);

        assertThat(answerMessage.getText()).isEqualTo("test");
    }

    /**
     * test checks that we get "unknown command" message if the user send unknown command;
     */
    @Test
    void test_should_get_unknown_command_msg(){

        when(userService.getUserByChatId(any())).thenReturn(new TgUser());
        when(messageSource.getMessage("unknownCommand",null,Locale.ENGLISH)).thenReturn("test");

        SendMessage answerMessage = messageHandler.answerMessage(message);

        assertThat(answerMessage.getText()).isEqualTo("test");
    }
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


}