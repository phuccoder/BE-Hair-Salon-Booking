package com.example.hairsalon.services;

import com.example.hairsalon.models.AccountEntity;
import com.example.hairsalon.requests.AccountRequest.AccountSignInRequest;
import com.example.hairsalon.requests.AccountRequest.AccountSignUpRequest;
import com.example.hairsalon.requests.AccountRequest.AccountUpdateRequest;
import com.example.hairsalon.responses.SignInResponse;

import java.util.ArrayList;

public interface IAccountService {
    void signUp(AccountSignUpRequest request);

    void verify(Long userId,String token);

    String refresh(String token);

    SignInResponse signIn(AccountSignInRequest request);

    AccountEntity getAccountByIdExceptAdmin(Long id);

    AccountEntity getAccountById(Long id);

    ArrayList<AccountEntity> getAllAccount();

    AccountEntity updatePersonalAccount(Long id, AccountUpdateRequest accountUpdateRequest);

    AccountEntity banUser(Long id);

}
