package com.ateng.mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RedisConfig {

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    /**
     * 原子扣库存：库存足够才扣减，返回扣减后剩余库存；不足返回 -1；key 不存在返回 -2。
     */
    @Bean
    public DefaultRedisScript<Long> deductStockScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<Long>();
        script.setResultType(Long.class);
        script.setScriptText(
                "local stock = redis.call('get', KEYS[1])\n" +
                        "if not stock then\n" +
                        "  return -2\n" +
                        "end\n" +
                        "stock = tonumber(stock)\n" +
                        "local qty = tonumber(ARGV[1])\n" +
                        "if stock < qty then\n" +
                        "  return -1\n" +
                        "end\n" +
                        "return redis.call('decrby', KEYS[1], qty)"
        );
        return script;
    }
}
