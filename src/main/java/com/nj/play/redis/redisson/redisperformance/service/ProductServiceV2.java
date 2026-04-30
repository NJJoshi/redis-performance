package com.nj.play.redis.redisson.redisperformance.service;

import com.nj.play.redis.redisson.redisperformance.entity.Product;
import com.nj.play.redis.redisson.redisperformance.util.CacheTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceV2 {

    @Autowired
    private CacheTemplate<Integer, Product> cacheTemplate;

    // GET
    public Mono<Product> getProduct(int id){
        return this.cacheTemplate.get(id);
    }

    // PUT
    public Mono<Product> updateProduct(int id, Mono<Product> productMono){
        return productMono
                .flatMap(p -> this.cacheTemplate.update(id, p));
    }

    // DELETE
    public Mono<Void> deleteProduct(int id){
        return this.cacheTemplate.delete(id);
    }
}
