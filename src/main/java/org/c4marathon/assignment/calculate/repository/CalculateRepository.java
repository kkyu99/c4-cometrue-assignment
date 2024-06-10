package org.c4marathon.assignment.calculate.repository;

import org.c4marathon.assignment.calculate.entity.CalculateId;
import org.c4marathon.assignment.calculate.entity.Calculates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CalculateRepository extends JpaRepository<Calculates, CalculateId> {

    @Query("select max(c.calculateId) from Calculates c")
    Optional<Integer> findLastId();
}
