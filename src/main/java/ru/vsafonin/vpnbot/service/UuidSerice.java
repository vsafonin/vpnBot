package ru.vsafonin.vpnbot.service;

import ru.vsafonin.vpnbot.entity.TgUser;
import ru.vsafonin.vpnbot.entity.Uuid;

public interface UuidSerice {

    TgUser getTgUserByUuid(String uuid);

    void save(Uuid uuid);
}
