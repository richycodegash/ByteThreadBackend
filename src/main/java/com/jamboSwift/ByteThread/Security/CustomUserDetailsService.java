package com.jamboSwift.ByteThread.Security;

import com.jamboSwift.ByteThread.Exceptions.ClientNotFoundException;
import com.jamboSwift.ByteThread.Models.Client;
import com.jamboSwift.ByteThread.Repo.ClientRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final ClientRepo clientRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client=clientRepo.findByEmail(email).orElseThrow(
                ()->new ClientNotFoundException("Account does not exist")
        );
        return new CustomUserPrincipal(client);
    }
}
