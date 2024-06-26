package org.c4marathon.assignment.concurrency.controller;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.calculate.entity.CalculateId;
import org.c4marathon.assignment.concurrency.service.ConcurrencyServiceImpl;
import org.c4marathon.assignment.transfer.entity.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/con")
@RequiredArgsConstructor
public class ConcurrencyController {

    private final ConcurrencyServiceImpl concurrencyService;


    @GetMapping ("/send")
    public ResponseEntity<?> send(@RequestParam Map<String, String> map, @RequestParam(name="sender") String id) throws Exception {

        //concurrencyService.transfer(map);
        concurrencyService.transfer2(map,id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
