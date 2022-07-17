package ru.vsafonin.vpnbot.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.vsafonin.vpnbot.enums.CommandType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * this class describes reply keyboard in the telegram
 */
@Component
public class ReplyKeyboard {
    /**
     * this method returns new keyboard for all users (not admin)
     * @param userTgLocale
     * @return new reply keyboard
     */
    protected ReplyKeyboardMarkup getReplyKeyboard(Locale userTgLocale){
        List<KeyboardRow> keyboardRows = getKeyBoardRowsForAllUsers(userTgLocale);
        return setTgKeyBoard(keyboardRows);
    }

    /**
     * this method returns net keyboard for admin users
     * @param userTgLocale
     * @return
     */
    public ReplyKeyboardMarkup getReplyKeyboardAdmin(Locale userTgLocale) {
        KeyboardButton spamMessage = new KeyboardButton(CommandType.SPAM_MESSAGE.getCommandName(userTgLocale));
        List<KeyboardRow> keyboardRows = getKeyBoardRowsForAllUsers(userTgLocale);
        return setTgKeyBoard(keyboardRows);
    }

    /*
    generates keyboard for all users
     */
    private List<KeyboardRow> getKeyBoardRowsForAllUsers(Locale userTgLocale){
        KeyboardButton getBalanceButton = new KeyboardButton(CommandType.GET_BALANCE.getCommandName(userTgLocale));
        KeyboardButton getEndDateButton = new KeyboardButton(CommandType.END_DATE.getCommandName(userTgLocale));
        KeyboardButton getVpnFileButton = new KeyboardButton(CommandType.GET_VPN_FILE.getCommandName(userTgLocale));
        KeyboardButton haveProblemButton = new KeyboardButton(CommandType.HAVE_PROBLEM.getCommandName(userTgLocale));
        KeyboardButton getPayLinkButton = new KeyboardButton(CommandType.PAY_LINK.getCommandName(userTgLocale));
        KeyboardButton addFriendButton = new KeyboardButton(CommandType.ADD_FRIEND.getCommandName(userTgLocale));

        KeyboardRow row1 = new KeyboardRow();
        row1.add(getBalanceButton);
        row1.add(getEndDateButton);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(getVpnFileButton);
        row2.add(haveProblemButton);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(getPayLinkButton);
        row3.add(addFriendButton);

        List<KeyboardRow> resultKeyBoard = new ArrayList<>(Arrays.asList(row1,row2,row3));
        return resultKeyBoard;
    }




    private ReplyKeyboardMarkup setTgKeyBoard(List<KeyboardRow> keyboardRows) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }
}
