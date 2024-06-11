package org.c4marathon.assignment.accounts.service;

import org.c4marathon.assignment.accounts.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceImplTest {


    @Test
    void transfer() throws InterruptedException {
        // given
        int threadCount = 100;
        RestTemplate restTemplate = new RestTemplate();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            final int ii = i;
            executorService.submit(() -> {
                try {
                    int port = 80;
                    ResponseEntity<Void> forEntity = restTemplate.getForEntity(
                            "http://localhost:" + port + "/user/charge?account=0&amount=10000",
                            Void.class);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }
}