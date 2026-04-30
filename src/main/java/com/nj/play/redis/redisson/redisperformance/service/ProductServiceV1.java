package com.nj.play.redis.redisson.redisperformance.service;

import com.nj.play.redis.redisson.redisperformance.entity.Product;
import com.nj.play.redis.redisson.redisperformance.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceV1 {

    @Autowired
    private ProductRepository repository;

    public Mono<Product> getProduct(Integer id) {
        return repository.findById(id);
    }

    public Mono<Product> updateProduct(int productId, Mono<Product> product) {
        return repository.findById(productId)
                .flatMap(p -> product.doOnNext(pr -> pr.setId(productId)))
                .flatMap(pr -> repository.save(pr));
    }
}
