package ru.vsafonin.vpnbot.telegram;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.vsafonin.vpnbot.service.UserService;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class MessageHandlerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private MessageSource messageSource;

    @InjectMocks
    private MessageHandler messageHandler;

    /**
     * test checks that we get "need registration" message if the user not in DB
     */
    @Test
    void test_should_get_need_register_msg(){
        Message message = mock(Message.class);
        User user = mock(User.class);
        when(message.getChatId()).thenReturn(1L);
        when(message.getFrom()).thenReturn(user);
        when(user.getLanguageCode()).thenReturn("EN");

        when(userService.getUserByChatId(any())).thenReturn(null);

        when(messageSource.getMessage("needRegistration",null, Locale.ENGLISH)).thenReturn("test");

        SendMessage answerMessage = messageHandler.answerMessage(message);

        assertThat(answerMessage.getText()).isEqualTo("test");
    }
}