package com.example.hairsalon.controllers;

import com.example.hairsalon.components.apis.CoreApiResponse;
import com.example.hairsalon.components.exceptions.ApiException;
import com.example.hairsalon.config.AppProperties;
import com.example.hairsalon.models.AccountEntity;
import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.requests.AccountRequest.AccountSignInRequest;
import com.example.hairsalon.requests.AccountRequest.AccountSignUpRequest;
import com.example.hairsalon.requests.AccountRequest.AccountUpdateRequest;
import com.example.hairsalon.requests.UserRefreshRequest;
import com.example.hairsalon.responses.RefreshResponse;
import com.example.hairsalon.responses.SignInResponse;
import com.example.hairsalon.services.IAccountService;
import com.example.hairsalon.services.IStylistService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${app.api.version.v1}/user")
public class UserController {
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IStylistService stylistService;

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

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/stylist/active")
    public List<StylistEntity> getActiveStylists() {
        return stylistService.getActiveStylists();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public CoreApiResponse<AccountEntity> updatePersonal(@PathVariable Long id, @RequestBody AccountUpdateRequest accountUpdateRequest) {
        return CoreApiResponse.success(accountService.updatePersonalAccount(id, accountUpdateRequest));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public CoreApiResponse<AccountEntity> getUserById(@PathVariable Long id) {
        return CoreApiResponse.success(accountService.getAccountByIdExceptAdmin(id));
    }

    @PostMapping("/refresh")
    public CoreApiResponse<?> refresh(
            @CookieValue(value = "refreshToken", required = false) String cookieRT,
            @RequestBody UserRefreshRequest bodyRT
    ) {
        if(bodyRT == null && !isValidToken(cookieRT)){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid token");
        }
        String token = bodyRT != null ? bodyRT.getRefreshToken() : cookieRT;

        String accessToken = accountService.refresh(token);

        return CoreApiResponse.success(new RefreshResponse(accessToken),"User refresh token successfully");
    }

    private boolean isValidToken(String token) {
        return token != null && isJWT(token);
    }

    private boolean isJWT(String token) {
        String[] parts = token.split("\\.");
        return parts.length == 3;
    }

}
