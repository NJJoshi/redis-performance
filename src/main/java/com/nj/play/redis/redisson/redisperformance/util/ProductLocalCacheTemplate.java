package com.nj.play.redis.redisson.redisperformance.util;

import com.nj.play.redis.redisson.redisperformance.entity.Product;
import com.nj.play.redis.redisson.redisperformance.repository.ProductRepository;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

//@Service
public class ProductLocalCacheTemplate extends CacheTemplate<Integer, Product> {

    @Autowired
    private ProductRepository repository;

    private RLocalCachedMap<Integer, Product> localCachedMap;

    public ProductLocalCacheTemplate(RedissonClient client) {
        LocalCachedMapOptions<Integer, Product> mapOptions = LocalCachedMapOptions.<Integer, Product>defaults()
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR);
        this.localCachedMap = client.getLocalCachedMap("product", new TypedJsonJacksonCodec(Integer.class, Product.class), mapOptions);
    }

    @Override
    protected Mono<Product> getFromSource(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    protected Mono<Product> getFromCache(Integer id) {
        return Mono.justOrEmpty(this.localCachedMap.get(id));
    }

    @Override
    protected Mono<Product> updateSource(Integer id, Product product) {
        return this.repository.findById(id)
                            .doOnNext(p -> product.setId(id))
                            .flatMap(p -> this.repository.save(product));
    }

    @Override
    protected Mono<Product> updateCache(Integer id, Product product) {
        return Mono.create(sink -> this.localCachedMap.fastPutAsync(id, product).thenAccept(b -> sink.success(product))
                .exceptionally(ex -> {
                    sink.error(ex);
                    return null;
                }));
    }

    @Override
    protected Mono<Void> deleteFromSource(Integer id) {
        return this.repository.deleteById(id);
    }

    @Override
    protected Mono<Void> deleteFromCache(Integer id) {
        return Mono.create(sink -> {
            this.localCachedMap.fastRemoveAsync(id).thenAccept(r -> sink.success()).exceptionally(ex -> {
                sink.error(ex);
                return null;
            });
        });
    }
}
