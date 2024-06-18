package org.c4marathon.assignment.calculate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.user.entity.UserEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="calculates")
@IdClass(CalculateId.class)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Calculates {
    @Id
    private Long calculateId;
    @Id
    @ManyToOne
    @JoinColumn(name="userId")
    private UserEntity receiverId;
    @ManyToOne
    @JoinColumn(name="account")
    private Account targetAccount;
    private Long amount;
    @CreationTimestamp
    private LocalDateTime date;

    @Builder
    public Calculates(Long calculateId, UserEntity receiverId, Account targetAccount, Long amount, LocalDateTime date) {
        this.calculateId = calculateId;
        this.receiverId = receiverId;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.date = date;
    }
}
