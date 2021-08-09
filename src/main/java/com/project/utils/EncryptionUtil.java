package com.project.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

/**
 * 加密工具类
 *
 * @author zhao
 */
public class EncryptionUtil {

    /**
     * 根据指定加密项生成编译器
     */
    private static final PasswordEncoder ENCODER = new Pbkdf2PasswordEncoder();
    /**
     * 加密
     *
     * @param rawPassword
     * @return
     */
    public static String encrypt(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    /**
     * 明文与加密后的密文匹配
     *
     * @param encryptedPassword 密文
     * @param password    明文
     * @return
     */
    public static boolean match(String password, String encryptedPassword) {
        return ENCODER.matches(password, encryptedPassword);
    }
}