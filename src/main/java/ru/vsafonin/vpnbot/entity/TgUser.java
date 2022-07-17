package ru.vsafonin.vpnbot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This entity store information about user.
 * For registration a new user, we generate special invite code, when user write this code to bot
 * we get his chat_id and store this user in DB;
 * By default user is inactive (field isActive - false), when user put money to balance
 * we set the isActive field to true;
 */
@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class TgUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    /**
     * Chat_id - the unique id from the Telegram,
     * with this id we can find our user and send message to the bot's chat
     */
    @Column(name = "chat_id")
    private Integer chatId;

    @Column(name = "name")
    @NotBlank(message = "{tgUser.validation.name.notBlank}")
    @Length(min = 3, max = 256, message = "{tgUser.validation.length}")
    private String name;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "isActive")
    private boolean isActive;

    @Column(name = "date_expired")
    private LocalDateTime dateExpired;

    @Column(name = "date_registration")
    private LocalDateTime dateRegistration;

    @Column(name = "description")
    @NotBlank(message = "{tgUser.validation.description.notBlank}")
    @Length(min = 3, max = 256, message = "{tgUser.validation.length}")
    private String inviterDescription;


    @Column(name = "invite_code")
    private String inviteCode;

    @Column(name = "isAdmin")
    private boolean isAdmin;

    @Override
    public String toString() {
        return "TgUser{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", isActive=" + isActive +
                ", dateExpired=" + dateExpired +
                ", dateRegistration=" + dateRegistration +
                ", description='" + inviterDescription + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TgUser tgUser = (TgUser) o;
        return isActive == tgUser.isActive && chatId.equals(tgUser.chatId) && name.equals(tgUser.name) && Objects.equals(balance, tgUser.balance) && Objects.equals(dateExpired, tgUser.dateExpired) && Objects.equals(dateRegistration, tgUser.dateRegistration) && Objects.equals(inviterDescription, tgUser.inviterDescription) && inviteCode.equals(tgUser.inviteCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, name, balance, isActive, dateExpired, dateRegistration, inviterDescription, inviteCode);
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }
}
