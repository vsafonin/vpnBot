package ru.vsafonin.vpnbot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "uuid")
public class Uuid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "tgUser_id", nullable = false)
    private TgUser tgUser;

    @Column(name = "uuid")
    private String uuid;

}
