package com.harry.order.common;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.StringJoiner;

@Component("queryKey")
public class QueryKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        // 你的方法签名类似：status, userId, productId, createdAfter, createdBefore, keyword, page, size
        StringJoiner sj = new StringJoiner("|");
        for (Object p : params) {
            sj.add(String.valueOf(p));
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(sj.toString().getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            return sj.toString(); // 兜底
        }
    }
}
