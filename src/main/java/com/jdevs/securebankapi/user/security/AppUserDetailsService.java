package com.jdevs.securebankapi.user.security;

import com.jdevs.securebankapi.user.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;

    public AppUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String normalizedEmail = username.trim().toLowerCase();

        return userRepository.findByEmail(normalizedEmail)
                .map(AppUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));
    }
}
