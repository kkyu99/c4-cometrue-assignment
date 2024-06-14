package org.c4marathon.assignment.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.apache.catalina.User;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.transfer.entity.Transfer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="user")
@Getter
@NoArgsConstructor
@ToString(exclude = {"password", "accounts"})
public class UserEntity {
    @Id
    private String userId;
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Account> accounts = new HashSet<>();

    public void add(Account account) {
        this.accounts.add(account);
    }

    @Builder
    public UserEntity(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    @Builder
    public UserEntity(String userId) {
        this.userId = userId;
    }

}
