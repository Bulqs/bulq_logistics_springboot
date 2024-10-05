package com.bulq.logistics.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bulq.logistics.models.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query("SELECT a FROM Wallet a WHERE " +
            "(:name IS NULL OR a.walletName LIKE CONCAT('%', :name, '%')) " +
            "AND (:walletId IS NULL OR a.id = :walletId)")
    Page<Wallet> findByWalletInfo(
            @Param("name") String name,
            @Param("walletId") Long walletId,
            Pageable pageable);

}

/**
 * 
 * 
 * private long id;
 * 
 * private String walletName;
 */
