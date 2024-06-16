package org.c4marathon.assignment.information.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.accounts.entity.Account;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="informations")
@Getter
@NoArgsConstructor
public class Informations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "owner_id")
    private Account owner;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private Account receiver;

    private String nickName;
    private Long amount;
    @CreationTimestamp
    private LocalDateTime date;


    @Builder
    public Informations(Account owner,Account sender, Account receiver, String nickName, Long amount) {
        this.owner = owner;
        this.sender = sender;
        this.receiver = receiver;
        this.nickName = nickName;
        this.amount = amount;
    }
}
