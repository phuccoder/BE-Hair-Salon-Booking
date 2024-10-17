package com.example.hairsalon.components.securities;

import com.example.hairsalon.models.AccountEntity;
import com.example.hairsalon.repositories.IAccountRepository;
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
    IAccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        AccountEntity user = accountRepository.findByAccountEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        AccountEntity user = accountRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}
