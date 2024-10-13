package com.example.hairsalon.services;

import com.example.hairsalon.requests.AccountSignInRequest;
import com.example.hairsalon.requests.AccountSignUpRequest;
import com.example.hairsalon.responses.SignInResponse;

public interface IAccountService {
    void signUp(AccountSignUpRequest request);

    SignInResponse signIn(AccountSignInRequest request);
}