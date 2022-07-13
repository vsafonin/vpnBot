package ru.vsafonin.vpnbot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * implementation of the Long Pooling Telegram bot
 * when user sends message to bot, we catch message in onUpdateReceived method
 * for registration bot in the Telegram, we need to fill both parameters in application.properties:
 * - botName
 * - botToken
 */
@Component
@Slf4j
public class LongPollBot extends TelegramLongPollingBot {
    private String botName;

    private String botToken;

    public LongPollBot(@Value("${telegram.name}") String botName,
                       @Value("${telegram.token}") String botToken) {
        this.botName = botName;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            execute(new SendMessage(update.getMessage().getChatId().toString(), "I'm, alive"));
        } catch (TelegramApiException e) {
            log.error(">>>> exceptional situation, when LongPollBot trying send message");
            throw new RuntimeException(e);
        }
    }
}
