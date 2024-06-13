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
    public Account chargeBalance(Map<String, String> map) throws Exception {

        Account account = accountRepository.findByAccountWithLock(map.get("account"))
                .orElseThrow(() -> new Exception("계좌없음"));

        account.setBalance(account.getBalance() + Integer.parseInt(map.get("amount")));

        return account;
    }

    @Transactional
    public Account sendCalculate(CalculateId calculateId) throws Exception {

        Calculates calculate = calculateRepository.findById(calculateId)
                .orElseThrow(() -> new Exception("잘못된 접근"));

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
            throw new Exception("정산 실패ㅜㅜ");
        }
        return result;
    }

    @Transactional
    public Account transfer(Map<String, String> map) throws Exception {

        long amount = Long.parseLong(map.get("amount"));

        Account sender = accountRepository.findByAccountWithLock(map.get("sender"))
                .orElseThrow(() -> new Exception("본인 계좌 없음"));

        if(sender.getBalance() < amount) {
            throw new Exception("잔액 부족");
        }

        if(sender.getTransferLimit() < amount) {
            throw new Exception("이체 한도 초과");
        }

        Account receiver =  accountRepository.findByAccountWithLock(map.get("receiver"))
                .orElseThrow(() -> new Exception("상대 계좌 없음"));

        sender.setBalance(sender.getBalance() - amount);
        sender.setTransferLimit(sender.getTransferLimit() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        return receiver;
    }
}
