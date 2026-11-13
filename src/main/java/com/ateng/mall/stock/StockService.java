package com.ateng.mall.stock;

import com.ateng.mall.common.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class StockService {

    public static final String STOCK_KEY_PREFIX = "mall:stock:";

    private final StringRedisTemplate stringRedisTemplate;
    private final DefaultRedisScript<Long> deductStockScript;

    public String stockKey(Long productId) {
        return STOCK_KEY_PREFIX + productId;
    }

    public void initStock(Long productId, int stock) {
        stringRedisTemplate.opsForValue().set(stockKey(productId), String.valueOf(stock));
    }

    public Integer getStock(Long productId) {
        String value = stringRedisTemplate.opsForValue().get(stockKey(productId));
        if (value == null) {
            return null;
        }
        return Integer.valueOf(value);
    }

    /**
     * Redis Lua 原子扣库存。
     *
     * @return 扣减后的剩余库存
     */
    public long deduct(Long productId, int quantity) {
        if (quantity <= 0) {
            throw new BizException("扣减数量必须大于 0");
        }
        Long result = stringRedisTemplate.execute(
                deductStockScript,
                Collections.singletonList(stockKey(productId)),
                String.valueOf(quantity)
        );
        if (result == null) {
            throw new BizException("扣库存失败：Redis 无响应");
        }
        if (result == -2L) {
            throw new BizException("库存未初始化，请重启服务或重新预热");
        }
        if (result == -1L) {
            throw new BizException("库存不足");
        }
        return result;
    }

    /** 下单失败时回滚 Redis 库存 */
    public void rollback(Long productId, int quantity) {
        stringRedisTemplate.opsForValue().increment(stockKey(productId), quantity);
    }
}
