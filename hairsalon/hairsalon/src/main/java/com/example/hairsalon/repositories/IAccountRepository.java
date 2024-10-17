package com.example.hairsalon.repositories;

import com.example.hairsalon.models.AccountEntity;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;

public interface IAccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAccountPhoneOrAccountEmail(String phone, String email);
    Optional<AccountEntity> findByAccountEmail(String email);
    Optional<AccountEntity> findByAccountPhone(String phone);

    @Query("SELECT a FROM AccountEntity a WHERE a.role <> 'admin'")
    ArrayList<AccountEntity> findAllUsersExceptAdmin();

    @Query("SELECT u FROM AccountEntity u WHERE u.accountID = :id AND u.role <> 'admin'")
    Optional<AccountEntity> findByIdExceptAdmin(@Param("id") Long id);
    Boolean existsByAccountEmail(String email);
    Boolean existsByAccountPhone(String phone);

}
