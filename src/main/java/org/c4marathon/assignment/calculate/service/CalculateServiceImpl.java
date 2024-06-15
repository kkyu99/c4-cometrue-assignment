package org.c4marathon.assignment.calculate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.repository.AccountRepository;
import org.c4marathon.assignment.calculate.entity.CalculateId;
import org.c4marathon.assignment.calculate.entity.Calculates;
import org.c4marathon.assignment.calculate.repository.CalculateRepository;
import org.c4marathon.assignment.user.entity.UserEntity;
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
        Long lastId = lastIdOptional.orElse(0) + 1L; // 데이터가 없는 경우 0을 기본값으로 사용
        long totalAmount = amount;
        long afterCalculate = 0;
        long calculatedAmount = 0;
        long additionalDeal = 0;
        if (type == 0) {
            calculatedAmount = totalAmount / (target.size() + 1);
            afterCalculate += calculatedAmount;
            for (String targetId : target) {
                afterCalculate += calculatedAmount;
                UserEntity targetUser = UserEntity.builder().userId(targetId).build();
                Calculates calculates = Calculates.builder()
                        .calculateId(lastId)
                        .receiverId(targetUser)
                        .targetAccount(account)
                        .amount(calculatedAmount)
                        .build();

                calculateRepository.save(calculates);
            }
            //빈 금액만큼 추가해줘
            additionalDeal = amount - afterCalculate;

        } else {
            for (String targetId : target) {
                int rand = (int) (Math.random() * 10 + 1);
                calculatedAmount = amount / rand;
                if (totalAmount < calculatedAmount) {
                    continue;
                }
                totalAmount -= calculatedAmount;
                UserEntity targetUser = UserEntity.builder().userId(targetId).build();
                Calculates calculates = Calculates.builder()
                        .calculateId(lastId)
                        .receiverId(targetUser)
                        .targetAccount(account)
                        .amount(calculatedAmount)
                        .build();

                calculateRepository.save(calculates);
            }
        }
        UserEntity targetUser = UserEntity.builder().userId(id).build();
        Calculates calculates = Calculates.builder()
                .calculateId(lastId)
                .receiverId(targetUser)
                .targetAccount(account)
                .amount(additionalDeal)
                .build();
        calculateRepository.save(calculates);
        return 1;
    }

}
