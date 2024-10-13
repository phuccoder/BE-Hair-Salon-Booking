package com.example.hairsalon.controllers;

import com.example.hairsalon.components.apis.CoreApiResponse;
import com.example.hairsalon.config.AppProperties;
import com.example.hairsalon.requests.AccountSignInRequest;
import com.example.hairsalon.requests.AccountSignUpRequest;
import com.example.hairsalon.responses.SignInResponse;
import com.example.hairsalon.services.IAccountService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${app.api.version.v1}/account")
public class AccountController {
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private IAccountService accountService;

    @PostMapping("/signup")
    public CoreApiResponse<?> signup(@Valid @RequestBody AccountSignUpRequest signUpRequest) {
        accountService.signUp(signUpRequest);
        return CoreApiResponse.success("User registered successfully");
    }

    @PostMapping("/signIn")
    public CoreApiResponse<SignInResponse> signIn(@Valid @RequestBody AccountSignInRequest request, HttpServletResponse response) {
        SignInResponse signIn = accountService.signIn(request);
        Cookie cookie = new Cookie("refreshToken", signIn.getRefreshToken());

        cookie.setMaxAge(appProperties.getAuth().getRefreshTokenExpirationMsec());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return CoreApiResponse.success(signIn);
    }


}
