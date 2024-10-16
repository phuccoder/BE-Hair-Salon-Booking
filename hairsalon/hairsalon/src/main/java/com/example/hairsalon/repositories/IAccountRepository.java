package com.example.hairsalon.repositories;

import com.example.hairsalon.models.AccountEntity;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAccountPhoneOrAccountEmail(String phone, String email);
    Optional<AccountEntity> findByAccountEmail(String email);
    Optional<AccountEntity> findByAccountPhone(String phone);
    Boolean existsByAccountEmail(String email);
    Boolean existsByAccountPhone(String phone);
}
