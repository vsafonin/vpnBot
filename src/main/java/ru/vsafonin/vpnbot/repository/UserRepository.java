package ru.vsafonin.vpnbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.vsafonin.vpnbot.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByChatId(Integer chatId);
}
