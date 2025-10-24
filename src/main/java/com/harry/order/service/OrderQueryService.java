package com.harry.order.service;

import com.harry.order.domain.Order;
import com.harry.order.domain.Product;
import com.harry.order.domain.User;
import com.harry.order.repository.OrderRepository;
import com.harry.order.repository.ProductRepository;
import com.harry.order.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderQueryService {

    @Autowired
    private final OrderRepository orderRepo;

    @Autowired
    private final UserRepository userRepo;

    @Autowired
    private final ProductRepository productRepo;

    public OrderQueryService(OrderRepository o, UserRepository u, ProductRepository p) {
        this.orderRepo = o;
        this.userRepo = u;
        this.productRepo = p;
    }

    @Cacheable(cacheNames = "order:byId", key = "#id")
    public Optional<Order> findOrderById(Long id) {
        return orderRepo.findById(id);
    }

    @Cacheable(cacheNames = "user:byId", key = "#id")
    public Optional<User> findUserById(Long id) {
        return userRepo.findById(id);
    }

    @Cacheable(cacheNames = "product:byId", key = "#id")
    public Optional<Product> findProductById(Long id) {
        return productRepo.findById(id);
    }
}
