package com.example.hairsalon.components.securities;

import com.example.hairsalon.config.AppProperties;
import com.example.hairsalon.components.exceptions.ApiException;
import com.example.hairsalon.models.TokenEntity;
import com.example.hairsalon.repositories.ITokenRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    @Autowired
    private ITokenRepository tokenRepository;
    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpirationMsec());

        String jwt = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();

        TokenEntity token = new TokenEntity();
        token.setExpirationDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        token.setExpired(false);
        token.setRevoked(false);
        token.setUser(userPrincipal.getUser());
        token.setName(jwt);

        tokenRepository.save(token);

        return jwt;
    }

    public String createAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getAccessTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }
    public String createAccessToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getAccessTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public String createToken(Long userId,int expiryTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiryTime);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            // Use parserBuilder instead of deprecated parser method
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(appProperties.getAuth().getTokenSecret())
                    .build();

            parser.parseClaimsJws(authToken); // Parse the JWT token
            return true;
        } catch (Exception ex) {
            if (ex instanceof SignatureException) {
                logger.error("Invalid JWT signature");
                throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid JWT signature");
            } else if (ex instanceof MalformedJwtException) {
                logger.error("Invalid JWT token");
                throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
            } else if (ex instanceof ExpiredJwtException) {
                logger.error("Expired JWT token");
                throw new ApiException(HttpStatus.UNAUTHORIZED, "Expired JWT token");
            } else if (ex instanceof UnsupportedJwtException) {
                logger.error("Unsupported JWT token");
                throw new ApiException(HttpStatus.UNAUTHORIZED, "Unsupported JWT token");
            } else if (ex instanceof IllegalArgumentException) {
                logger.error("JWT claims string is empty.");
                throw new ApiException(HttpStatus.UNAUTHORIZED, "JWT claims string is empty.");
            }
        }
        return false;
    }

}
