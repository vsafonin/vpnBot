package ru.vsafonin.vpnbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsafonin.vpnbot.entity.TgUser;
import ru.vsafonin.vpnbot.entity.Uuid;
import ru.vsafonin.vpnbot.repository.UuidRepository;
@Service
public class UuidSericeImpl implements UuidSerice {
    @Autowired
    private UuidRepository uuidRepository;

    @Override
    public TgUser getTgUserByUuid(String uuid) {
        return  uuidRepository.getTgUserByUuid(uuid);
    }

    @Override
    public void save(Uuid uuid) {
        uuidRepository.save(uuid);
    }
}
