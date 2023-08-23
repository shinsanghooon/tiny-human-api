package com.tinyhuman.tinyhumanapi.auth.service;

import com.tinyhuman.tinyhumanapi.auth.config.EmailAuthentication;
import com.tinyhuman.tinyhumanapi.auth.config.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService jpaUserDetailService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        SecurityUser userDetails = (SecurityUser) jpaUserDetailService.loadUserByUsername(username);
        if (!userDetails.checkPassword(password, passwordEncoder)) {
            throw new BadCredentialsException("인증에 실패했습니다.");
        }
        return new EmailAuthentication(username, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

