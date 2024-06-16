package org.c4marathon.assignment.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.entity.AccountType;
import org.c4marathon.assignment.accounts.service.AccountServiceImpl;
import org.c4marathon.assignment.calculate.entity.CalculateId;
import org.c4marathon.assignment.calculate.service.CalculateServiceImpl;
import org.c4marathon.assignment.transfer.entity.Transfer;
import org.c4marathon.assignment.user.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.c4marathon.assignment.user.entity.UserEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserServiceImpl userService;
    private final AccountServiceImpl accountService;
    private final CalculateServiceImpl calculateService;


    @PostMapping("/join")
    public ResponseEntity<?> save(@RequestBody UserEntity userEntity) throws InterruptedException {
        System.out.println(userEntity);
        Account result = userService.join(userEntity);
        return new ResponseEntity<Account>(result, HttpStatus.OK);
    }


    //    @GetMapping("/charge")
//    public ResponseEntity<?> chargeBalance(@RequestParam Map<String,String> map) throws Exception {
//
//        Account result = accountService.chargeBalance(map);
//
//
//        return new ResponseEntity<Account>(result,HttpStatus.OK);
//    }

}
