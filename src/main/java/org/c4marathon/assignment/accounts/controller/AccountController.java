package org.c4marathon.assignment.accounts.controller;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.entity.AccountType;
import org.c4marathon.assignment.accounts.service.AccountServiceImpl;
import org.c4marathon.assignment.calculate.entity.CalculateId;
import org.c4marathon.assignment.transfer.entity.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountServiceImpl accountService;
    @GetMapping("/send/cal")
    public ResponseEntity<?> sendCalculate
            (@RequestParam(name = "calculateId") Long calId,
             @RequestParam(name = "receiverId") String receiverId
            ) throws Exception {
        CalculateId calculateId = new CalculateId(calId, receiverId);
        System.out.println(calculateId);
        Transfer result = accountService.sendCalculate(calculateId);
        return new ResponseEntity<Integer>(1, HttpStatus.OK);
    }

    @GetMapping("/charge")
    public void chargeBalance(@RequestParam Map<String, String> map) throws Exception {

        Account result = accountService.chargeBalance(map);


    }
    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestParam Map<String, String> map) throws Exception {

        Transfer result = accountService.transfer(map);

        return new ResponseEntity<Transfer>(result, HttpStatus.OK);
    }

    @GetMapping("/add")
    public ResponseEntity<?> addAcount(@RequestParam(name = "id") String id, @RequestParam(name = "type") AccountType type) {

        Account result = accountService.addAcount(id, type);


        return new ResponseEntity<Account>(result, HttpStatus.OK);
    }

}
