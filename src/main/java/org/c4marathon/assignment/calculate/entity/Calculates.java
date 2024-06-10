package org.c4marathon.assignment.calculate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="calculates")
@IdClass(CalculateId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Calculates {
    @Id
    private int calculateId;
    @Id
    private String receiverId;
    private int targetAccount;
    private long amount;
    @CreationTimestamp
    private LocalDateTime date;

    public Calculates(int calculatedId, String receiverId, int targetAccount, long amount) {
        this.calculateId = calculatedId;
        this.receiverId = receiverId;
        this.targetAccount = targetAccount;
        this.amount = amount;
    }
}
