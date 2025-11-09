package com.harry.order.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class UserPasswordEncodeTest {

    @Test
    void generateEncodedPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123456"; // 明文密码
        String encodedPassword = encoder.encode(rawPassword);

        log.info("Encoded password {} into {}", rawPassword, encodedPassword);
    }
}
