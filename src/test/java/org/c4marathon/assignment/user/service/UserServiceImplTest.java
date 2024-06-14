package org.c4marathon.assignment.user.service;

import org.apache.catalina.User;
import org.assertj.core.api.Assertions;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Test
    @Transactional
    void join() {
        UserEntity user = UserEntity.builder()
                .userId("aaa")
                .password("1234")
                .build();
        Account account = userService.join(user);
        // account의 userentity가 user와 동일한지 확인

        user.add(account);
        for(Account a : user.getAccounts()) {
            System.out.println(a);
        }
        Assertions.assertThat(account.getUser()).isEqualTo(user);
    }
}