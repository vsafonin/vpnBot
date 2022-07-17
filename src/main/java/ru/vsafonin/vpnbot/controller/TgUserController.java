package ru.vsafonin.vpnbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vsafonin.vpnbot.entity.TgUser;
import ru.vsafonin.vpnbot.service.UserService;
import ru.vsafonin.vpnbot.service.UuidSerice;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * this controller we are use for create, modify and delete user
 */
@Controller
public class TgUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;
    @Autowired
    private UuidSerice uuidSerice;

    @GetMapping("/newTgUser/{uuid}")
    public String getNewTgUser(@PathVariable("uuid")String uuid, Model model){
        TgUser inviter = uuidSerice.getTgUserByUuid(uuid);
        if (inviter == null) {
            return "404";
        }
        TgUser tgUser = new TgUser();
        model.addAttribute("tgUser", tgUser);
        return "TgUser";
    }

    /**
     * validations user field name and description, if ok
     * checks, may be user with same name is exist? if not register new user
     * @param tgUser
     * @return
     */
    @PostMapping("/newTgUser")
    public String saveNewTgUser(@Valid @ModelAttribute("tgUser")TgUser tgUser,
                                BindingResult bindingResult,
                                Model model){
        if (bindingResult.hasErrors()){
            return "TgUser";
        }
        TgUser tgUserInDb = userService.getUserByName(tgUser.getName());
        if (tgUserInDb == null) {

            String inviteCode = UUID.randomUUID().toString();
            tgUser.setInviteCode(inviteCode);
            tgUser.setActive(false);
            tgUser.setAdmin(false);
            tgUser.setBalance(BigDecimal.ZERO);
            userService.save(tgUser);
            String tgBotName = env.getProperty("telegram.name");
            StringBuilder activationLink = new StringBuilder();
            activationLink.append("https").append("://")
                            .append("t.me/")
                                    .append(tgBotName)
                                            .append("?start=")
                                                    .append(inviteCode);
            model.addAttribute("activationLink", activationLink.toString());
            model.addAttribute("invite", inviteCode);

        }
        else {
            FieldError error = new FieldError("tgUser","name","User is exist"); //TODO add internalization
            bindingResult.addError(error);
            return "TgUser";
        }
        return "Success";
    }
}
