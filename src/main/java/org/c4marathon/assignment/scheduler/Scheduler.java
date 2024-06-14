package org.c4marathon.assignment.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.repository.AccountRepository;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@PropertySource(value={"classpath:config/schedule.properties"})
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final AccountRepository accountRepository;

    @Scheduled(cron = "${schedule.limit.cron}")
    @Async
    @Transactional
    public void setTransferLimit() {
        final long CHARGE_LIMIT = 3000000L;
        List<Account> accounts = accountRepository.findAll();
        for(Account account : accounts) {
            account.setChargeLimit(CHARGE_LIMIT);
        }
        log.info("transferLimit updated");
    }
}
