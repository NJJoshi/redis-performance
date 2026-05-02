package com.nj.play.redis.redisson.redisperformance.config;

import org.redisson.Redisson;
import org.redisson.api.NatMapper;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.redisson.misc.RedisURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class RedissonConfig {
    private RedissonClient redissonClient;

    @Bean
    public RedissonClient getRedissonClient() {
        if(Objects.isNull(redissonClient)) {
            Config config = new Config();
            config.useSentinelServers()
                    .setMasterName("mymaster")
                    .addSentinelAddress("redis://127.0.0.1:26380")
                    .addSentinelAddress("redis://127.0.0.1:26381")
                    .addSentinelAddress("redis://127.0.0.1:26382")
                    .setCheckSentinelsList(false) // Crucial because Sentinels are in +sdown
                    .setConnectTimeout(10000)
                    .setTimeout(10000)
                    .setNatMapper(new NatMapper() {
                        @Override
                        public RedisURI map(RedisURI uri) {
                            // This logic is the 'bridge'
                            // It takes '172.20.0.2' and returns '127.0.0.1'
                            // But it KEEPS the port (6380, 6381, 6382)
                            return new RedisURI(uri.getScheme(), "127.0.0.1", uri.getPort());
                        }
                    });

            redissonClient = Redisson.create(config);
        }
        return redissonClient;
    }

    @Bean
    public RedissonReactiveClient getRedissonReactiveClient() {
        return getRedissonClient().reactive();
    }
}
