package com.example.hairsalon.controllers;

import com.example.hairsalon.components.apis.CoreApiResponse;
import com.example.hairsalon.models.AccountEntity;
import com.example.hairsalon.requests.AccountRequest.AccountSignUpRequest;
import com.example.hairsalon.requests.AccountRequest.AccountUpdateRequest;
import com.example.hairsalon.services.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("${app.api.version.v1}/account")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AccountController {
    @Autowired
    IAccountService accountService;
    @GetMapping("/getAll")
    public CoreApiResponse<ArrayList<AccountEntity>> getAllUser() {
        return CoreApiResponse.success(accountService.getAllAccount());
    }

    @GetMapping("/{id}")
    public CoreApiResponse<AccountEntity> getUserById(@PathVariable Long id) {
        return CoreApiResponse.success(accountService.getAccountById(id));
    }

    @PutMapping("/{id}")
    public CoreApiResponse<AccountEntity> updateUser(@PathVariable Long id, @RequestBody AccountUpdateRequest accountUpdateRequest) {
        return CoreApiResponse.success(accountService.updatePersonalAccount(id, accountUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<AccountEntity> banUser(@PathVariable Long id) {
        return CoreApiResponse.success(accountService.banUser(id));
    }

    @PostMapping("/create/{role}")
    public CoreApiResponse<?> createAccount(@RequestBody AccountSignUpRequest accountSignUpRequest, @PathVariable String role) {
        accountService.signUpByRole(accountSignUpRequest, role);
        return CoreApiResponse.success("Create Successfully! Please check mail to verify!");
    }

}
