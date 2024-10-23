package com.example.hairsalon.components.securities;

import com.example.hairsalon.models.AccountEntity;
import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.repositories.IAccountRepository;
import com.example.hairsalon.repositories.IStylistRepository;
import com.example.hairsalon.components.exceptions.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private IStylistRepository stylistRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String emailOrPhone) throws UsernameNotFoundException {
        // Try to find the user in AccountEntity first
        AccountEntity account = accountRepository.findByAccountPhoneOrAccountEmail(emailOrPhone, emailOrPhone)
                .orElse(null);

        if (account != null) {
            return UserPrincipal.create(account);  // Return UserPrincipal for AccountEntity
        }

        // If not found in AccountEntity, search in StylistEntity
        StylistEntity stylist = stylistRepository.findByStylistEmailOrStylistPhone(emailOrPhone, emailOrPhone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email/phone: " + emailOrPhone));

        return UserPrincipal.create(stylist);  // Return UserPrincipal for StylistEntity
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        // Try to find the user in AccountEntity first
        AccountEntity account = accountRepository.findById(id).orElse(null);

        if (account != null) {
            return UserPrincipal.create(account);  // Return UserPrincipal for AccountEntity
        }

        // If not found in AccountEntity, search in StylistEntity
        StylistEntity stylist = stylistRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(stylist);  // Return UserPrincipal for StylistEntity
    }
}
