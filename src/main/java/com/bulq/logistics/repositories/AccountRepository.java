package com.bulq.logistics.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bulq.logistics.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByOtp(String token);

    @Query("SELECT a FROM Account a WHERE " +
        "(:name IS NULL OR a.firstName LIKE CONCAT('%', :name, '%') OR a.lastName LIKE CONCAT('%', :name, '%')) " +
        "OR (:username IS NULL OR a.username LIKE CONCAT('%', :username, '%')) " +
        "AND (:phoneNumber IS NULL OR a.phoneNumber LIKE CONCAT('%', :phoneNumber, '%')) " +
        "AND (:email IS NULL OR a.email LIKE CONCAT('%', :email, '%')) " +
        "AND (:authorities IS NULL OR a.authorities LIKE CONCAT('%', :authorities, '%'))")
Page<Account> findByUserInfo(
    @Param("name") String name,
    @Param("username") String username,
    @Param("authorities") String authorities,
    @Param("phoneNumber") String phoneNumber,
    @Param("email") String email,
    Pageable pageable
);
}
