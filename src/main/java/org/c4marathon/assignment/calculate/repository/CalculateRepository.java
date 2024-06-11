package org.c4marathon.assignment.calculate.repository;

import jakarta.persistence.LockModeType;
import org.c4marathon.assignment.calculate.entity.CalculateId;
import org.c4marathon.assignment.calculate.entity.Calculates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CalculateRepository extends JpaRepository<Calculates, CalculateId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    <S extends Calculates> S save(S entity);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select max(c.calculateId) from Calculates c")
    Optional<Integer> findLastId();
}
