package org.c4marathon.assignment.information.controller;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.information.service.InformationServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info")
public class InformationController {

    private final InformationServiceImpl informationService;


}
