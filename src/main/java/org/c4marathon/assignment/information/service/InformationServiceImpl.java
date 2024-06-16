package org.c4marathon.assignment.information.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.information.repository.InformationsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InformationServiceImpl {

    private final InformationsRepository informationsRepository;
}
