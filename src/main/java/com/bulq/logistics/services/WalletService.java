package com.bulq.logistics.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bulq.logistics.models.Booking;
import com.bulq.logistics.models.Transaction;
import com.bulq.logistics.models.Wallet;
import com.bulq.logistics.repositories.WalletRepository;
import com.bulq.logistics.util.constants.InsufficientBalanceException;
import com.bulq.logistics.util.constants.TransactionStatus;
import com.bulq.logistics.util.constants.TransactionType;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    public Wallet save(Wallet wallet) {
        Wallet savedWallet = walletRepository.save(wallet);

        Transaction transaction = new Transaction();
        transaction.setAmount(wallet.getBalance());
        transaction.setWallet(wallet);
        transaction.setRecipient(wallet.getAccount().getFullName());
        transaction.setTransactionDate(LocalDateTime.now());
        // transaction.setTransactionStatus(TransactionStatus.NEW);
        // transaction.setTransactionType(TransactionType.JUSTCREATEDNEWWALLET);
        transactionService.save(transaction);

        return savedWallet;
    }

    // @Transactional
    // public Wallet createAndFundWallet(Wallet wallet, BigDecimal amount) {
    //     Wallet savedWallet = walletRepository.save(wallet);
    //     Transaction transaction = new Transaction();
    //     transaction.setWallet(wallet);
    //     transactionService.save(transaction);
    //     transactionService.createTransaction(wallet, amount);
    //     return savedWallet;
    // }

    @Transactional
    public void deductFromWallet(Long walletId, Booking booking, BigDecimal amount) throws InsufficientBalanceException {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // check that the balance is enough for the deduction
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in wallet for this deduction.");
        }

        // Deduct the amount from the balance
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        // Write history to transaction table
        Transaction transaction = new Transaction();
        transaction.setAmount(wallet.getBalance());
        transaction.setWallet(wallet);
        transaction.setRecipient(wallet.getAccount().getFullName());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setBooking(booking);
        transactionService.save(transaction);
    }

    /**
     * Method to fund a wallet by adding a specific amount to the current balance
     *
     * @param walletId the ID of the wallet to fund
     * @param amount   the amount to add to the wallet
     * @throws RuntimeException if wallet is not found or the amount is invalid
     */

    @Transactional
    public void fundWallet(Long walletId, BigDecimal amount) throws RuntimeException {
        // Find wallet by ID
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found!"));

        // Check if the amount is valid (greater than 0)
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Funding amount must be greater than zero");
        }

        // Add the amount to the current wallet balance
        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);

        // Save the updated wallet balance
        walletRepository.save(wallet);

        // Write history to transaction table

        Transaction transaction = new Transaction();
        transaction.setAmount(wallet.getBalance());
        transaction.setWallet(wallet);
        transaction.setRecipient(wallet.getAccount().getFullName());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionType(TransactionType.FUND);
        transactionService.save(transaction);
    }

    public Optional<Wallet> findById(Long walletId) {
        return walletRepository.findById(walletId);
    }

    public Page<Wallet> findByWalletInfo(int page, int pageSize, String sortBy, String name, Long walletId) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        return walletRepository.findByWalletInfo(name, walletId, pageable);
    }
}
