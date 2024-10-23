package com.example.hairsalon.components.securities;

import com.example.hairsalon.models.AccountEntity;
import com.example.hairsalon.models.StylistEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UserPrincipal implements UserDetails {
    private AccountEntity user;
    private StylistEntity stylist;

    public UserPrincipal(AccountEntity user) {
        this.user = user;
    }

    private Collection<? extends GrantedAuthority> authorities;


    public UserPrincipal(AccountEntity user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    // Constructor for StylistEntity
    public UserPrincipal(StylistEntity stylist, Collection<? extends GrantedAuthority> authorities) {
        this.stylist = stylist;
        this.authorities = authorities;
    }

    public static UserPrincipal create(AccountEntity user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_"+user.getRole().toUpperCase()));

        return new UserPrincipal(
                user,
                authorities
        );
    }

    // Create method for StylistEntity
    public static UserPrincipal create(StylistEntity stylist) {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + stylist.getRole().toUpperCase())
        );
        return new UserPrincipal(stylist, authorities);
    }

    public Long getId() {
        return user != null ? user.getAccountID() : stylist.getStylistID();
    }

    public String getEmail() {
        return user != null ? user.getAccountEmail() : stylist.getStylistEmail();
    }

    @Override
    public String getPassword() {
        return user != null ? user.getPassword() : stylist.getStylistPassword();
    }

    @Override
    public String getUsername() {
        return user != null ? user.getAccountName() : stylist.getStylistName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}