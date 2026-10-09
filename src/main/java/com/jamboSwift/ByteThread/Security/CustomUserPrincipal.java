package com.jamboSwift.ByteThread.Security;

import com.jamboSwift.ByteThread.Enums.ClientStatus;
import com.jamboSwift.ByteThread.Models.Client;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class CustomUserPrincipal implements UserDetails {

    private final Client client;

    public Client getClient() {
        return client;
    }

    @Override
    public String getUsername() {
        return client.getEmail();
    }

    @Override
    public String getPassword() {
        return client.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + client.getRole()));
    }

    @Override
    public boolean isEnabled() {
        return client.getStatus() == ClientStatus.ACTIVE;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }
}
