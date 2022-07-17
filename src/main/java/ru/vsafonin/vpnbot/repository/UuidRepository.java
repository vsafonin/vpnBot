package ru.vsafonin.vpnbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.vsafonin.vpnbot.entity.TgUser;
import ru.vsafonin.vpnbot.entity.Uuid;

public interface UuidRepository extends JpaRepository<Uuid,Integer> {
    @Query("Select tgUser from Uuid u where u.uuid = :uuid")
    TgUser getTgUserByUuid(@Param("uuid") String uuid);
}
