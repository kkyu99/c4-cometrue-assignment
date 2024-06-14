package org.c4marathon.assignment.calculate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.repository.AccountRepository;
import org.c4marathon.assignment.calculate.entity.CalculateId;
import org.c4marathon.assignment.calculate.entity.Calculates;
import org.c4marathon.assignment.calculate.repository.CalculateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculateServiceImpl {

    private final CalculateRepository calculateRepository;
    private final AccountRepository accountRepository;


    @Transactional
    public int requestCalculate(List<String> target, String id, long amount, int type) {
        //대상, 정산 금액, 보낼 계좌
        Account account = accountRepository.findMainAccountById(id);
        Optional<Integer> lastIdOptional = calculateRepository.findLastId();
        int lastId = lastIdOptional.orElse(0)+1; // 데이터가 없는 경우 0을 기본값으로 사용
        long totalAmount = amount;
        long afterCalculate = 0;
        long calculatedAmount = 0;
        if(type==0) {
            calculatedAmount = totalAmount / (target.size()+1);
            afterCalculate += calculatedAmount;
            for(String targetId : target) {
                afterCalculate += calculatedAmount;
                calculateRepository.save(new Calculates(lastId,targetId,account.getAccount(),calculatedAmount));
            }
            //빈 금액만큼 추가해줘
            long additionalDeal = amount - afterCalculate;
            account.setBalance(account.getBalance() + additionalDeal);
        } else {
            for(String targetId : target) {
                int rand = (int) (Math.random() * 10);
                calculatedAmount = amount / rand;
                if(totalAmount < calculatedAmount) {
                    continue;
                }
                totalAmount -= calculatedAmount;
                calculateRepository.save(new Calculates(lastId,targetId,account.getAccount(),calculatedAmount));
            }
        }

        return 1;
    }

}