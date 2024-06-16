package org.c4marathon.assignment.information.repository;

import org.c4marathon.assignment.information.entity.Informations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationsRepository extends JpaRepository<Informations,Long> {
}
