package org.c4marathon.assignment.concurrency.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.repository.AccountRepository;
import org.c4marathon.assignment.transfer.entity.Transfer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcurrencyServiceImpl {

    private final AccountRepository accountRepository;

    @Transactional
    public void transfer(Map<String, String> map) throws RuntimeException{
        
        long amount = Long.parseLong(map.get("amount"));
        Account sender = accountRepository.findByAccountWithLock(map.get("sender"))
                .orElseThrow(() -> new RuntimeException("본인 계좌 없음"));
//        Account sender = accountRepository.findById(map.get("sender"))
//                .orElseThrow(() -> new RuntimeException("본인 계좌 없음"));

        if (sender.getBalance() < amount) {
            throw new RuntimeException("잔액 부족");
        }

        Account receiver = accountRepository.findByAccountWithLock(map.get("receiver"))
                .orElseThrow(() -> new RuntimeException("상대 계좌 없음"));
//        Account receiver = accountRepository.findById(map.get("receiver"))
//                .orElseThrow(() -> new RuntimeException("상대 계좌 없음"));

        sender.updateBalance(sender.getBalance() - amount);
        receiver.updateBalance(receiver.getBalance() + amount);
    }


}
