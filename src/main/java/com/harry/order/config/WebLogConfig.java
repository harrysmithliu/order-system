package com.harry.order.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class WebLogConfig implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Around("execution(* com.harry.order.controller..*(..))")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        long startTime = System.currentTimeMillis();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        Object[] args = joinPoint.getArgs();

        // 打印请求信息
        log.info("[REQUEST] Method: {} | Path: {} | Params: {}",
                method, uri, queryString != null ? queryString : "none");

        if (args.length > 0) {
            try {
                log.info("[REQUEST-BODY] {}", objectMapper.writeValueAsString(args));
            } catch (Exception e) {
                log.info("[REQUEST-BODY] JSON serialization failed {}", args);
            }
        }

        // 执行方法
        Object result = joinPoint.proceed();

        // 打印响应信息
        long duration = System.currentTimeMillis() - startTime;
        log.info("[RESPONSE] Path: {} | Duration: {}ms", uri, duration);

        try {
            log.info("[RESPONSE-BODY] {}", objectMapper.writeValueAsString(result));
        } catch (Exception e) {
            log.info("[RESPONSE-BODY] JSON serialization failed {}", result);
        }

        return result;
    }
}
