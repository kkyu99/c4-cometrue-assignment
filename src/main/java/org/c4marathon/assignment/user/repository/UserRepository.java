package org.c4marathon.assignment.user.repository;

import jakarta.persistence.LockModeType;
import org.c4marathon.assignment.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface UserRepository extends JpaRepository<UserEntity,Integer>{

}
