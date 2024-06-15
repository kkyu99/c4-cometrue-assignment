package org.c4marathon.assignment.transfer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.transfer.entity.Transfer;
import org.c4marathon.assignment.transfer.service.TransferServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
@Slf4j
public class TransferController {

    private final TransferServiceImpl transferService;

    @GetMapping("/accept")
    public ResponseEntity acceptTransfer(
            @RequestParam Map<String,String> map
            ) {
        int result = transferService.acceptTransfer(map);
        return new ResponseEntity<Integer>(result, HttpStatus.OK);
    }

    @GetMapping("/cancel")
    public ResponseEntity cancelTransfer(
            @RequestParam Map<String,String> map
    ) {
        Account result = transferService.cancelTransfer(map);
        return new ResponseEntity<Account>(result, HttpStatus.OK);
    }

}
