package org.c4marathon.assignment.transfer.repository;

import jakarta.persistence.LockModeType;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.transfer.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer,Long> {
    List<Transfer> findBySenderAccountAndCalculateId(Long senderAccount, Long calculateId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value="SELECT a FROM Transfer a where a.id = :id")
    Optional<Transfer> findByIdWithLock(@Param("id") Long id);
}
