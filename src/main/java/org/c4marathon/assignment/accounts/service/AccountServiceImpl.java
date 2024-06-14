package org.c4marathon.assignment.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.entity.AccountType;
import org.c4marathon.assignment.accounts.repository.AccountRepository;
import org.c4marathon.assignment.calculate.entity.CalculateId;
import org.c4marathon.assignment.calculate.entity.Calculates;
import org.c4marathon.assignment.calculate.repository.CalculateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl {
    private final AccountRepository accountRepository;
    private final CalculateRepository calculateRepository;

    public Account addAcount(String id , AccountType type) {
        Account account = new Account(0,id,0,3000000,type);
        return accountRepository.save(account);
    }

    @Transactional
    public Account chargeBalance(Map<String, String> map) throws RuntimeException {

        Account account = accountRepository.findByAccountWithLock(map.get("account"))
                .orElseThrow(() -> new RuntimeException("계좌없음"));

        int amount = Integer.parseInt(map.get("amount"));

        if(account.getChargeLimit() < amount) {
            throw new RuntimeException("충전 한도 초과");
        }

        account.setBalance(account.getBalance() + amount);
        account.setChargeLimit(account.getChargeLimit() - amount);
        return account;
    }

    @Transactional
    public Account sendCalculate(CalculateId calculateId) throws RuntimeException {

        Calculates calculate = calculateRepository.findById(calculateId)
                .orElseThrow(() -> new RuntimeException("잘못된 접근"));

        //내 계좌 찾기
        int sender = accountRepository.findMainAccountById(calculateId.getReceiverId()).getAccount();
        System.out.println(calculate);
        System.out.println(sender);
        // 송금하기
        Map<String,String> map = new HashMap<>();
        map.put("amount", String.valueOf(calculate.getAmount()));
        map.put("sender", String.valueOf(sender));
        map.put("receiver", String.valueOf(calculate.getTargetAccount()));

        Account result = transfer(map);
        if(result != null) {
            calculateRepository.delete(calculate);
        } else {
            throw new RuntimeException("정산 실패ㅜㅜ");
        }
        return result;
    }

    @Transactional
    public Account transfer(Map<String, String> map) throws RuntimeException {

        long amount = Long.parseLong(map.get("amount"));

        Account sender = accountRepository.findByAccountWithLock(map.get("sender"))
                .orElseThrow(() -> new RuntimeException("본인 계좌 없음"));

        if(sender.getBalance() < amount) {
            throw new RuntimeException("잔액 부족");
        }

        Account receiver =  accountRepository.findByAccountWithLock(map.get("receiver"))
                .orElseThrow(() -> new RuntimeException("상대 계좌 없음"));

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        return receiver;
    }
}
