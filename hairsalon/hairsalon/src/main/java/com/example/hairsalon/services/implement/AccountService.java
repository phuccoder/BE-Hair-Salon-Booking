package com.example.hairsalon.services.implement;

import com.example.hairsalon.components.exceptions.ApiException;
import com.example.hairsalon.components.securities.TokenProvider;
import com.example.hairsalon.components.securities.UserPrincipal;
import com.example.hairsalon.models.AccountEntity;
import com.example.hairsalon.repositories.IAccountRepository;
import com.example.hairsalon.requests.AccountSignInRequest;
import com.example.hairsalon.requests.AccountSignUpRequest;
import com.example.hairsalon.responses.SignInResponse;
import com.example.hairsalon.services.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;



import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final IAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public SignInResponse signIn(AccountSignInRequest request) {
        // Attempt to find the user by either email or phone
        Optional<AccountEntity> accountOptional = accountRepository.findByAccountPhoneOrAccountEmail(request.getEmailOrPhone(), request.getEmailOrPhone());

        if (!accountOptional.isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid phone/email or password");
        }

        AccountEntity account = accountOptional.get();

        // Now authenticate using the retrieved account's credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        account.getAccountEmail(),  // or account.getAccountPhone()
                        request.getPassword()
                )
        );

        // Proceed with checking if account is deactivated
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if (!userPrincipal.getUser().isAccountStatus()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Account is deactivated");
        }

        // Set the authentication in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate the tokens
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accountID(userPrincipal.getId())
                .accountName(userPrincipal.getUsername())
                .role(userPrincipal.getUser().getRole())
                .build();
    }

    @Override
    public void signUp(AccountSignUpRequest request) {
        // Check if the account exists based on phone or email
        Optional<AccountEntity> accountOptional = accountRepository.findByAccountPhoneOrAccountEmail(request.getAccountPhone(), request.getAccountEmail());

        if (accountOptional.isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Account with this phone or email already exists");
        }

        if(!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Password and Confirm Password is not match");
        }

        // Create and populate new account entity
        AccountEntity account = new AccountEntity();
        account.setAccountName(request.getAccountName());
        account.setAccountEmail(request.getAccountEmail());
        account.setAccountPhone(request.getAccountPhone());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setRole("user"); // Assign default role

        // Save the new account to the repository
        accountRepository.save(account);
    }
}
