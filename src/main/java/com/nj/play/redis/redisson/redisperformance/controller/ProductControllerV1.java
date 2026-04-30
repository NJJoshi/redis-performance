package com.nj.play.redis.redisson.redisperformance.controller;

import com.nj.play.redis.redisson.redisperformance.entity.Product;
import com.nj.play.redis.redisson.redisperformance.service.ProductServiceV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("product/v1")
public class ProductControllerV1 {

    @Autowired
    private ProductServiceV1 service;

    @GetMapping("{id}")
    public Mono<Product> getProduct(@PathVariable int id) {
        return service.getProduct(id);
    }

    @PutMapping("{id}")
    public Mono<Product> updateProduct(@PathVariable int id, @RequestBody Mono<Product> product) {
        return service.updateProduct(id, product);
    }
}
