package org.c4marathon.assignment.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.repository.AccountRepository;
import org.c4marathon.assignment.transfer.entity.Transfer;
import org.c4marathon.assignment.transfer.repository.TransferRepository;
import org.c4marathon.assignment.transfer.service.TransferServiceImpl;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@PropertySource(value={"classpath:config/schedule.properties"})
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final TransferServiceImpl transferService;
    @Scheduled(cron = "${schedule.limit.cron}")
    @Async
    @Transactional
    public void setChargeLimit() {
        final long CHARGE_LIMIT = 3000000L;
        List<Account> accounts = accountRepository.findAll();
        for(Account account : accounts) {
            account.updateChargeLimit(CHARGE_LIMIT);
        }
        log.info("transferLimit updated");
    }

    //TODO: !!
    @Scheduled(cron = "${schedule.transfer.cron}")
    @Async
    @Transactional
    public void checkTransferRemainTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Transfer> transfers = transferRepository.findAll(Sort.by(Sort.Direction.ASC, "date"));
        log.info("스케줄링!!" + transfers.size());
        for(Transfer transfer : transfers) {
            LocalDateTime transferDate = transfer.getDate();
            Duration duration = Duration.between(currentTime,transferDate);
            log.info(duration.toString());
            if(duration.toHours() == 48) {
                log.info(transfer.getReceiver().getUser().getUserId() + "24시간 전!!");
            }
            if(duration.toHours() == 72) {
                transferService.cancelByScheduling(transfer);
            }

        }

    }
}
