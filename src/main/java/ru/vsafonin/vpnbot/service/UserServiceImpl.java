package ru.vsafonin.vpnbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsafonin.vpnbot.entity.User;
import ru.vsafonin.vpnbot.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public User getUserByChatId(Integer chatId) {
        return userRepository.findByChatId(chatId);
    }
}
