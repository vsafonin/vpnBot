package ru.vsafonin.vpnbot.service;

import ru.vsafonin.vpnbot.entity.TgUser;

import java.math.BigDecimal;

public interface UserService{

    TgUser getUserByChatId(Integer chatId);

    BigDecimal getBalance(Integer chatId);
}
