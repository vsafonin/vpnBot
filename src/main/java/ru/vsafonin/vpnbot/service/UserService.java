package ru.vsafonin.vpnbot.service;

import ru.vsafonin.vpnbot.entity.User;

public interface UserService{

    User getUserByChatId(Integer chatId);

}
