package org.c4marathon.assignment.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.entity.AccountType;
import org.c4marathon.assignment.accounts.repository.AccountRepository;
import org.c4marathon.assignment.calculate.entity.CalculateId;
import org.c4marathon.assignment.calculate.entity.Calculates;
import org.c4marathon.assignment.calculate.repository.CalculateRepository;
import org.c4marathon.assignment.information.entity.Informations;
import org.c4marathon.assignment.information.repository.InformationsRepository;
import org.c4marathon.assignment.transfer.entity.Transfer;
import org.c4marathon.assignment.transfer.repository.TransferRepository;
import org.c4marathon.assignment.user.entity.UserEntity;
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
    private final TransferRepository transferRepository;
    private final InformationsRepository informationsRepository;

    public Account addAcount(String id, AccountType type) {
        final Long CHARGE_LIMIT = 3000000L;
        //Account account = new Account(0,id,0,3000000,type);
        UserEntity user = UserEntity.builder().userId(id).build();
        Account account = Account.builder()
                .user(user)
                .balance(0L)
                .chargeLimit(CHARGE_LIMIT)
                .accountType(type)
                .build();
        user.getAccounts().add(account);
        return accountRepository.save(account);
    }

    @Transactional
    public Account chargeBalance(Map<String, String> map) throws RuntimeException {

        Account account = accountRepository.findByAccountWithLock(map.get("account"))
                .orElseThrow(() -> new RuntimeException("계좌없음"));

        Long amount = Long.parseLong(map.get("amount"));
        account.updateBalance(account.getBalance() + amount);
        account.updateChargeLimit(account.getChargeLimit() - amount);

        saveInformation(account,amount,account,"나",account);

        return account;
    }

    @Transactional
    public Transfer sendCalculate(CalculateId calculateId) throws RuntimeException {
        Calculates calculate = calculateRepository.findById(calculateId)
                .orElseThrow(() -> new RuntimeException("잘못된 접근"));
        //내 계좌 찾기
        Long sender = accountRepository.findMainAccountById(calculateId.getReceiverId()).getAccount();
        //이미 보냈으면 취소
        if (transferRepository.findBySenderAccountAndCalculateId(sender, calculateId.getCalculateId()).size() != 0) {
            throw new RuntimeException("이미 정산 중...");
        }
        // 송금하기
        Map<String, String> map = new HashMap<>();
        map.put("amount", String.valueOf(calculate.getAmount()));
        map.put("sender", String.valueOf(sender));
        map.put("receiver", String.valueOf(calculate.getTargetAccount().getAccount()));
        map.put("calculateId", String.valueOf(calculateId.getCalculateId()));

        return transfer(map);
    }

    @Transactional
    public Transfer transfer(Map<String, String> map) throws RuntimeException {

        long amount = Long.parseLong(map.get("amount"));
        Account sender = accountRepository.findByAccountWithLock(map.get("sender"))
                .orElseThrow(() -> new RuntimeException("본인 계좌 없음"));

        if (sender.getBalance() < amount) {
            throw new RuntimeException("잔액 부족");
        }
        String nickName = setNickName(map, sender);
        Account receiver = accountRepository.findByAccountWithLock(map.get("receiver"))
                .orElseThrow(() -> new RuntimeException("상대 계좌 없음"));
        sender.updateBalance(sender.getBalance() - amount);
        Long calculateId = null;
        if (map.containsKey("calculateId")) {
            calculateId = Long.parseLong(map.get("calculateId"));
        }
        return saveTransfer(calculateId, amount, sender, nickName, receiver);
    }

    @Transactional
    public Transfer saveTransfer(Long calculateId, long amount, Account sender, String nickName, Account receiver) {
        Transfer transfer = Transfer.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .nickName(nickName).build();
        if(calculateId != null) {
            transfer.setCalculateId(calculateId);
        }
        transferRepository.save(transfer);
        saveInformation(sender, amount, sender, nickName, receiver);
        return transfer;
    }

    @Transactional
    public void saveInformation(Account owner, long amount, Account sender, String nickName, Account receiver) {
        Informations information = Informations.builder()
                .owner(owner)
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .nickName(nickName).build();

        informationsRepository.save(information);
    }


    private static String setNickName(Map<String, String> map, Account sender) {
        String nickName = sender.getUser().getUserId();
        if(map.containsKey("nickName")) {
            nickName = map.get("nickName");
        }
        return nickName;
    }
}
