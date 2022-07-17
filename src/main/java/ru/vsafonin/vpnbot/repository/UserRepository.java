package ru.vsafonin.vpnbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.vsafonin.vpnbot.entity.TgUser;

import java.math.BigDecimal;

public interface UserRepository extends JpaRepository<TgUser,Integer> {

    TgUser findByChatId(Integer chatId);

    @Query("Select balance from TgUser u where u.chatId = :chatId")
    BigDecimal getBalanceByChatId(@Param("chatId") Integer chatId);
    @Query("Select u From TgUser u where u.inviteCode = :inviteCode")
    TgUser getUserByInviteCode(@Param("inviteCode") String inviteCode);

    TgUser getUserByName(String name);
}
