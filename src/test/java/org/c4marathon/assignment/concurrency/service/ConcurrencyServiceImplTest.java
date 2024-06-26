package org.c4marathon.assignment.concurrency.service;

import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.repository.AccountRepository;
import org.c4marathon.assignment.accounts.service.AccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConcurrencyServiceImplTest {
    @Autowired
    ConcurrencyServiceImpl concurrencyService;
    @Autowired
    AccountRepository accountRepository;

    @Test
    void transfer2() {
    }

    private void transferTest(Consumer<Void> action) throws InterruptedException {
        Long originBalance = accountRepository.findById("16").orElseThrow().getBalance();

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    action.accept(null);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Account account = accountRepository.findById("16").orElseThrow();
        assertEquals(originBalance - 100, account.getBalance());
    }

    @Test
    @DisplayName("동시에 100번의 송금 : 분산락")
    public void redissonTicketingTest() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("sender","16");
        map.put("receiver","17");
        map.put("amount","1");
        transferTest((_no) -> concurrencyService.transfer2(map,"16"));
    }
}