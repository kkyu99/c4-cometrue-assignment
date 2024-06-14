package org.c4marathon.assignment.transfer.service;

import org.assertj.core.api.Assertions;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.repository.AccountRepository;
import org.c4marathon.assignment.accounts.service.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransferServiceImplTest {

    @Autowired
    private TransferServiceImpl transferService;
    @Autowired
    private AccountRepository accountRepository;
    @Test
    @Transactional
    void acceptTransfer() {

        Map<String,String> map = new HashMap<>();
        map.put("userId","aaa");
        map.put("transferId","6");
        Account before = accountRepository.findById("4").orElse(null);
        Long balance = before.getBalance();
        Account account = transferService.acceptTransfer(map);

        Assertions.assertThat(balance).isEqualTo(account.getBalance() - 10000);

    }

    @Test
    @Transactional
    void cancelTransfer() {
        Map<String,String> map = new HashMap<>();
        map.put("userId","a1");
        map.put("transferId","6");
        Account before = accountRepository.findById("13").orElse(null);
        Long balance = before.getBalance();
        Account account = transferService.cancelTransfer(map);

        Assertions.assertThat(balance).isEqualTo(account.getBalance() - 10000);
    }
}