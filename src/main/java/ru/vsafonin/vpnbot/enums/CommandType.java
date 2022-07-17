package ru.vsafonin.vpnbot.enums;

import java.util.Locale;
import java.util.ResourceBundle;

public enum CommandType {
    GET_BALANCE("getBalance"),
    END_DATE("endDate"),
    GET_VPN_FILE("getVpnFile"),
    HAVE_PROBLEM("haveProblem"),
    PAY_LINK("payLink"),
    ADD_FRIEND("addFriend"),
    SPAM_MESSAGE("spamMessage")
    ;


    private String commandName;

    CommandType(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName(Locale userLocale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages",userLocale);
        String resultCommandName = resourceBundle.getString("keyboard.button." + this.commandName);
        return resultCommandName;
    }




}
