package ru.vsafonin.vpnbot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
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
public class User {

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
    private String description;


    @Column(name = "invite_code")
    private String inviteCode;

    @Override
    public String toString() {
        return "User{" +
                "chat_id=" + chatId +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", isActive=" + isActive +
                ", dateExpired=" + dateExpired +
                ", dateRegistration=" + dateRegistration +
                ", description='" + description + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isActive == user.isActive && chatId.equals(user.chatId) && name.equals(user.name) && Objects.equals(balance, user.balance) && Objects.equals(dateExpired, user.dateExpired) && Objects.equals(dateRegistration, user.dateRegistration) && Objects.equals(description, user.description) && inviteCode.equals(user.inviteCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, name, balance, isActive, dateExpired, dateRegistration, description, inviteCode);
    }
}
