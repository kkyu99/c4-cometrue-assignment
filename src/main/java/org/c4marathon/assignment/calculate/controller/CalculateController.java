package org.c4marathon.assignment.calculate.controller;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.calculate.entity.CalculateId;
import org.c4marathon.assignment.calculate.service.CalculateServiceImpl;
import org.c4marathon.assignment.transfer.entity.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cal")
public class CalculateController {


    private final CalculateServiceImpl calculateService;

    @GetMapping("/cal")
    public ResponseEntity<?> requestCalculate
            (@RequestParam(name = "id") String id,
             @RequestParam(name = "targets") List<String> targets,
             @RequestParam(name = "amount") long amount,
             @RequestParam(name = "type") int type
            ) {
        int result = calculateService.requestCalculate(targets, id, amount, type);
        return new ResponseEntity<Integer>(result, HttpStatus.OK);
    }
}
