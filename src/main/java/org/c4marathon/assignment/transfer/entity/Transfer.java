package org.c4marathon.assignment.transfer.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.c4marathon.assignment.accounts.entity.Account;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="transfers")
@Getter
@NoArgsConstructor
@ToString
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private Account receiver;
    private Long amount;
    @CreationTimestamp
    private LocalDateTime date;

    @Nullable
    private Long calculateId;
    @Builder
    public Transfer(Account sender, Account receiver, Long amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public void setCalculateId(Long calculateId) {
        this.calculateId = calculateId;
    }
}
