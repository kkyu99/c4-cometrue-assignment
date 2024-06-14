package org.c4marathon.assignment.user.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.entity.AccountType;
import org.c4marathon.assignment.accounts.repository.AccountRepository;
import org.c4marathon.assignment.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.c4marathon.assignment.user.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    @Transactional
    public Account join(UserEntity userEntity) {
        final Long CHARGE_LIMIT = 3000000L;
        userRepository.save(userEntity);
        Account account = Account.builder()
                .user(userEntity)
                .balance(0L)
                .chargeLimit(CHARGE_LIMIT)
                .accountType(AccountType.MAIN)
                .build();
        return accountRepository.save(account);
    }
}
