package ru.vsafonin.vpnbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsafonin.vpnbot.entity.TgUser;
import ru.vsafonin.vpnbot.repository.UserRepository;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public TgUser getUserByChatId(Integer chatId) {
        return userRepository.findByChatId(chatId);
    }

    @Override
    public BigDecimal getBalance(Integer chatId) {
        return userRepository.getBalanceByChatId(chatId);
    }

    @Override
    public TgUser getUserByInviteCode(String inviteCode) {
        return userRepository.getUserByInviteCode(inviteCode);
    }

    @Override
    public void save(TgUser tgUser) {
        userRepository.save(tgUser);
    }

    @Override
    public TgUser getUserByName(String name) {
        return userRepository.getUserByName(name);
    }
}
