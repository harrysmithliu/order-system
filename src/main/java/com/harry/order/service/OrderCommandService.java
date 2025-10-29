package com.harry.order.service;

import com.harry.order.domain.Order;
import com.harry.order.repository.OrderRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
public class OrderCommandService {
    private final OrderRepository repo;

    public OrderCommandService(OrderRepository repo) {
        this.repo = repo;
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "order:byId", key = "#result.id", condition = "#result != null"),
            @CacheEvict(cacheNames = "order:pages", allEntries = true) // 页缓存整体失效（简单可靠）
    })
    public Order create(Order o) {
        return repo.save(o);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "order:byId", key = "#id"),
            @CacheEvict(cacheNames = "order:pages", allEntries = true)
    })
    public void cancel(Long id) {
        repo.deleteById(id);
    }

    // 更新状态、发货、支付等都同理做 Evict
}
