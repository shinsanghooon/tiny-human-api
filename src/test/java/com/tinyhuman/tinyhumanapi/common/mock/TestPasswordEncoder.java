package com.tinyhuman.tinyhumanapi.common.mock;

import org.springframework.security.crypto.password.PasswordEncoder;

public class TestPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString() + "_encoded";
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String encodedRawPassword = rawPassword.toString() + "_encoded";
        return encodedRawPassword.equals(encodedPassword);
    }

}
