package com.example.hairsalon.services.implement;

import com.example.hairsalon.components.events.MailEvent;
import com.example.hairsalon.components.exceptions.ApiException;
import com.example.hairsalon.components.exceptions.DataNotFoundException;
import com.example.hairsalon.components.mapper.AccountMapper;
import com.example.hairsalon.components.securities.TokenProvider;
import com.example.hairsalon.components.securities.UserPrincipal;
import com.example.hairsalon.models.AccountEntity;
import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.models.TokenEntity;
import com.example.hairsalon.repositories.IAccountRepository;
import com.example.hairsalon.repositories.IStylistRepository;
import com.example.hairsalon.repositories.ITokenRepository;
import com.example.hairsalon.requests.AccountRequest.AccountSignInRequest;
import com.example.hairsalon.requests.AccountRequest.AccountSignUpRequest;
import com.example.hairsalon.requests.AccountRequest.AccountUpdateRequest;
import com.example.hairsalon.responses.SignInResponse;
import com.example.hairsalon.services.IAccountService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;


import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final IAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    ITokenRepository tokenRepository;

    @Value("${app.fe.verify_url}")
    private String verifyUrl;

//    @Value("${app.fe.forgot_password_url}")
//    private String forgotPasswordUrl;

    @Override
    public SignInResponse signIn(AccountSignInRequest request) {

        // Authenticate using the provided credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmailOrPhone(),
                        request.getPassword()
                )
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Check if it's an account or a stylist and perform corresponding checks
        if (userPrincipal.getUser() != null) {
            // For AccountEntity
            AccountEntity account = userPrincipal.getUser();

            if (!account.getEmailVerified()) {
                sendVerifyMail(account);
                throw new ApiException(HttpStatus.BAD_REQUEST, "Email not verified");
            }

            if (!account.isAccountStatus()) {
                throw new ApiException(HttpStatus.FORBIDDEN, "Account is deactivated");
            }

        } else if (userPrincipal.getStylist() != null) {
            // For StylistEntity
            StylistEntity stylist = userPrincipal.getStylist();

            // Assume stylists don't need email verification, modify based on your logic
            if (!stylist.getStylistStatus()) {
                throw new ApiException(HttpStatus.FORBIDDEN, "Stylist account is deactivated");
            }
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid user type");
        }

        // Generate tokens
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        // Set the authentication in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String role = "user";
        String roleUserPrincipal = userPrincipal.getAuthorities().toString();
        if(roleUserPrincipal.equals("[ROLE_STYLIST]")) {
            role = "stylist";
        } else if(roleUserPrincipal.equals("[ROLE_ADMIN]")) {
            role = "admin";
        } else if(roleUserPrincipal.equals("[ROLE_STAFF]")) {
            role = "staff";
        } else if(roleUserPrincipal.equals("[ROLE_MANAGER]")) {
            role = "manager";
        }

        // Build the response based on user type
        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accountID(userPrincipal.getId()) // works for both Account and Stylist
                .accountName(userPrincipal.getUsername()) // works for both Account and Stylist
                .role(role) // Works for both Account and Stylist
                .build();
    }


    @Override
    public ArrayList<AccountEntity> getAllAccount() {
        return accountRepository.findAllUsersExceptAdmin();
    }


    @Override
    public AccountEntity getAccountByIdExceptAdmin(Long id) {
        return accountRepository.findByIdExceptAdmin(id)
                .orElseThrow(()
                        -> new DataNotFoundException("User", "id", id));
    }

    @Override
    public AccountEntity getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User", "id", id));
    }

    @Override
    public AccountEntity updatePersonalAccount(Long id, AccountUpdateRequest update) {
        AccountEntity existingUser = accountRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("User", "id", id));

        accountMapper.updatePersonalFromRequest(update, existingUser);

        if (update.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(update.getPassword()));
        }

        return accountRepository.save(existingUser);
    }

    @Override
    public void signUp(AccountSignUpRequest request) {
        // Check if the account exists based on phone or email
        Optional<AccountEntity> accountOptional = accountRepository.findByAccountEmail(request.getAccountEmail());

        if (accountOptional.isPresent()) {
            AccountEntity account = accountOptional.get();
            if(account.getEmailVerified()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Account with this phone or email already exists");
            } else {
                sendVerifyMail(account);
                return;
            }
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
        account.setEmailVerified(false);
        account.setRole("user");

        // Save the new account to the repository
        accountRepository.save(account);
        sendVerifyMail(account);
    }

    @Override
    public String refresh(String token) {
        TokenEntity refreshToken = tokenRepository.findByName(token)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.BAD_REQUEST,"Refresh Token not found with token : " + token)
                );
        if(refreshToken.isRevoked()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Token đã bị thu hồi");
        }
        if(refreshToken.isExpired()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Token đã hết hạn");
        }
        if(refreshToken.getExpirationDate().isBefore(LocalDate.now())){
            refreshToken.setExpired(true);
            throw new ApiException(HttpStatus.BAD_REQUEST,"Token đã hết hạn");
        }

        String accessToken = tokenProvider.createAccessToken(refreshToken.getUser().getAccountID());

        return accessToken;
    }

    @Override
    public void verify(Long userId, String token) {
        tokenProvider.validateToken(token);
        AccountEntity user = accountRepository.findById(userId)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with id : " + userId)
                );
        user.setEmailVerified(true);

        accountRepository.save(user);
    }

    @Override
    public AccountEntity banUser(Long id) {
        AccountEntity existUser = accountRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User", "id", id));

        existUser.setAccountStatus(false);
        return accountRepository.save(existUser);
    }

    @Override
    public void signUpByRole(AccountSignUpRequest request, String role) {
        // Check if the account exists based on phone or email
        Optional<AccountEntity> accountOptional = accountRepository.findByAccountEmail(request.getAccountEmail());

        if (accountOptional.isPresent()) {
            AccountEntity account = accountOptional.get();
            if(account.getEmailVerified()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Account with this phone or email already exists");
            }
        }

        if(!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Password and Confirm Password is not match");
        }

        if(role.equals("admin")) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You cant create admin");
        }

        if(role.equals("user")){
            throw new ApiException(HttpStatus.FORBIDDEN, "You cant create user with admin");
        }

        if(role.equals("stylist")) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Stylist need to create in stylist api");
        }

        // Create and populate new account entity
        AccountEntity account = new AccountEntity();
        account.setAccountName(request.getAccountName());
        account.setAccountEmail(request.getAccountEmail());
        account.setAccountPhone(request.getAccountPhone());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setEmailVerified(true);
        account.setRole(role.toLowerCase());

        // Save the new account to the repository
        accountRepository.save(account);
    }

    private void sendVerifyMail(AccountEntity user) {
        String token = tokenProvider.createToken(user.getAccountID(), 300000); // 5 minutes
        String urlPattern = verifyUrl + "?userId={0}&token={1}";
        String url = MessageFormat.format(urlPattern, user.getAccountID(), token);
        applicationEventPublisher.publishEvent(new MailEvent(this, user, url, "verify"));
    }
}
