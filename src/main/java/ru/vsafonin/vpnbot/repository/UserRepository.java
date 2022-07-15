package ru.vsafonin.vpnbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsafonin.vpnbot.entity.TgUser;

import java.math.BigDecimal;

public interface UserRepository extends JpaRepository<TgUser,Integer> {

    TgUser findByChatId(Integer chatId);

    BigDecimal getBalanceByChatId(Integer chatId);
}
