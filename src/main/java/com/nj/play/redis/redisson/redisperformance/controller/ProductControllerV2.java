package com.nj.play.redis.redisson.redisperformance.controller;

import com.nj.play.redis.redisson.redisperformance.entity.Product;
import com.nj.play.redis.redisson.redisperformance.service.ProductServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("product/v2")
public class ProductControllerV2 {

    @Autowired
    private ProductServiceV2 service;

    @GetMapping("{id}")
    public Mono<Product> getProduct(@PathVariable int id){
        return this.service.getProduct(id);
    }

    @PutMapping("{id}")
    public Mono<Product> updateProduct(@PathVariable int id, @RequestBody Mono<Product> productMono){
        return this.service.updateProduct(id, productMono);
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteProduct(@PathVariable int id){
        return this.service.deleteProduct(id);
    }
}
