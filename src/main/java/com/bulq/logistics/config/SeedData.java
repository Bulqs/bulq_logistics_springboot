package com.bulq.logistics.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bulq.logistics.models.Account;
import com.bulq.logistics.models.ServiceCategory;
import com.bulq.logistics.models.Wallet;
import com.bulq.logistics.util.constants.ServiceCatalog;
import com.bulq.logistics.services.AccountService;
import com.bulq.logistics.services.ServiceCategoryService;
import com.bulq.logistics.services.WalletService;
import com.bulq.logistics.util.constants.Authority;

@Component
public class SeedData implements CommandLineRunner {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ServiceCategoryService categoryService;

    @Autowired
    private WalletService walletService;

    @Override
    public void run(String... args) throws Exception {
        Account account01 = new Account();
        Account account02 = new Account();
        Account account03 = new Account();

        ServiceCategory category01 = new ServiceCategory();
        ServiceCategory category02 = new ServiceCategory();
        ServiceCategory category03 = new ServiceCategory();

        Wallet wallet01 = new Wallet();
        Wallet wallet02 = new Wallet();
        Wallet wallet03 = new Wallet();

        account01.setEmail("user@user.com");
        account01.setPassword("password");
        account01.setAuthorities(Authority.USER.toString());
        accountService.save(account01);

        account02.setEmail("admin@admin.com");
        account02.setPassword("password");
        account02.setAuthorities(Authority.ADMIN.toString());
        accountService.save(account02);

        account03.setEmail("business@business.com");
        account03.setPassword("password");
        account03.setAuthorities(Authority.BUSINESS.toString());
        accountService.save(account03);

        category01.setServiceName("Pick Up Package");
        category01.setServiceDescription("Get your Package delivered anywhere");
        category01.setPrice(40);
        category01.setServiceCode(ServiceCatalog.PUP);
        categoryService.save(category01);

        category02.setServiceName("Deliver Package");
        category02.setServiceDescription("Deliver Package");
        category02.setPrice(40);
        category02.setServiceCode(ServiceCatalog.DP);
        categoryService.save(category02);

        category03.setServiceName("Book a drop off");
        category03.setServiceDescription("Book a drop off at any of our nearby hubs");
        category03.setPrice(30);
        category03.setServiceCode(ServiceCatalog.BADO);
        categoryService.save(category03);

        // wallet01.setWalletName("Olahammed");
        // wallet01.setAccount(account01);
        // walletService.save(wallet01);

        // wallet02.setWalletName("Redux");
        // wallet02.setAccount(account02);

        // wallet03.setWalletName("GQ");
        // wallet03.setAccount(account03);
    }

    
}
