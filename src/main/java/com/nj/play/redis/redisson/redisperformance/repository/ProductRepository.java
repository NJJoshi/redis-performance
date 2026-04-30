package com.nj.play.redis.redisson.redisperformance.repository;

import com.nj.play.redis.redisson.redisperformance.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
}
