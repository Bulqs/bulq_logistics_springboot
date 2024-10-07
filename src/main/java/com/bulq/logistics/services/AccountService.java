package com.bulq.logistics.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bulq.logistics.models.Account;
import com.bulq.logistics.repositories.AccountRepository;
import com.bulq.logistics.util.auth.CustomUserDetails;
import com.bulq.logistics.util.constants.Authority;

@Service
public class AccountService implements UserDetailsService {

    
    
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getAuthorities() == null) {
            account.setAuthorities(Authority.USER.toString());
        };

        return accountRepository.save(account);
    }

    public List<Account> findAll(){
        return accountRepository.findAll();
    }

    public Optional<Account> findByEmail(String email){
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> findById(Long id){
        return accountRepository.findById(id);
    }

    public Optional<Account> findByOtp(String token){
        return accountRepository.findByOtp(token);
    }

    public void deleteById(Long id){
        accountRepository.deleteById(id);
    }

    public Page<Account> findAccounts(int page, int pageSize, String sortBy, String name, String username, String authorities, String phoneNumber, String email){
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        return accountRepository.findByUserInfo(name, username, authorities, phoneNumber, email, pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("Account not found!");
        }
        Account account = optionalAccount.get();
        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getAuthorities()));
        // return new User(account.getEmail(), account.getPassword(), grantedAuthority);
        // Return custom user details including first name and authorities
        return new CustomUserDetails(account.getEmail(), account.getPassword(), account.getFirstName(), grantedAuthority);
    }

}
