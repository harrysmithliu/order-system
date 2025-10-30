package com.harry.order.service;

import com.harry.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest // 使用你项目的 RedisCacheConfig；本地 Redis 已映射 6379
public class OrderServiceCacheTest {

    @Autowired
    OrderService service;
    @SpyBean
    OrderRepository repo;  // 监控底层调用
    @Autowired
    CacheManager cacheManager;

    @Test
    void shouldHitCache_onSecondCall() {
        service.getOrderSummaries(null, null, null, null, null, "", 0, 10);
        service.getOrderSummaries(null, null, null, null, null, "", 0, 10);

        // 验证 repository 只被调用一次（第二次命中缓存）
        verify(repo, times(1)).findOrderSummaries(any(), any(), any(), any(), any(), any(), any());
    }
}
