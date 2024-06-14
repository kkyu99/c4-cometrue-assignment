package org.c4marathon.assignment.accounts.entity;

import jakarta.persistence.*;
import lombok.*;
import org.c4marathon.assignment.transfer.entity.Transfer;
import org.c4marathon.assignment.user.entity.UserEntity;

import java.util.List;

@Entity
@Table(name="accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long account;

    @ManyToOne
    @JoinColumn(name = "id")
    private UserEntity user;
    private Long balance;
    private Long chargeLimit;
    @Enumerated(EnumType.ORDINAL)
    private AccountType accountType;

    @OneToMany(mappedBy = "sender")
    private List<Transfer> sentTransfers;

    @OneToMany(mappedBy = "receiver")
    private List<Transfer> receivedTransfers;

    @Builder
    public Account(UserEntity user, Long balance, Long chargeLimit, AccountType accountType) {
        this.user = user;
        this.balance = balance;
        this.chargeLimit = chargeLimit;
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "Account[account: " + account + ", user: " + user.getUserId() + " ]";
    }

    public void updateBalance(Long balance) {
        this.balance = balance;
    }
    public void updateChargeLimit(Long chargeLimit) {
        this.chargeLimit = chargeLimit;
    }
}
