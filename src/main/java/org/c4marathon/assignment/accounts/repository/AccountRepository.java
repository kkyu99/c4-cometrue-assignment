package org.c4marathon.assignment.accounts.repository;

import jakarta.persistence.LockModeType;
import org.c4marathon.assignment.accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    <S extends Account> S save(S entity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    List<Account> findAll();

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query(value="SELECT a FROM Account a where a.user.userId = :userId and a.accountType=0")
    Account findMainAccountById(@Param("userId") String id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value="SELECT a FROM Account a where a.account = :account")
    Optional<Account> findByAccountWithLock(@Param("account") String account);


}
