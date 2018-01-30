package com.xiaolyuh.service;

import com.xiaolyuh.lock.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 扣库存
 *
 * @author yuhao.wang
 */
@Service
public class StockService {
    Logger logger = LoggerFactory.getLogger(StockService.class);

    /**
     * 库存不足
     */
    public static final int LOW_STOCK = 0;

    /**
     * 不限库存
     */
    public static final long UNINITIALIZED_STOCK = -1L;

    /**
     * Redis 客户端
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 执行扣库存的脚本
     */
    public static final String STOCK_LUA;

    static {
        /**
         *
         * @desc 扣减库存Lua脚本
         * 库存（stock）-1：表示不限库存
         * 库存（stock）0：表示没有库存
         * 库存（stock）大于0：表示剩余库存
         *
         * @params 库存key
         * @return
         * 		0:库存不足
         * 		-1:库存未初始化
         * 		大于0:剩余库存（扣减之前剩余的库存）
         * 	    redis缓存的库存(value)是-1表示不限库存，直接返回1
         */
        StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('exists', KEYS[1]) == 1) then");
        sb.append("    local stock = tonumber(redis.call('get', KEYS[1]));");
        sb.append("    if (stock == -1) then");
        sb.append("        return 1;");
        sb.append("    end;");
        sb.append("    if (stock > 0) then");
        sb.append("        redis.call('incrby', KEYS[1], -1);");
        sb.append("        return stock;");
        sb.append("    end;");
        sb.append("    return 0;");
        sb.append("end;");
        sb.append("return -1;");
        STOCK_LUA = sb.toString();
    }

    /**
     * @param key           库存key
     * @param expire        库存有效时间,单位秒
     * @param stockCallback 初始化库存回调函数
     * @return 0:库存不足; -1:库存未初始化; 大于0:扣减库存之前的剩余库存（扣减之前剩余的库存）
     */
    public long stock(String key, long expire, IStockCallback stockCallback) {
        long stock = stock(key);
        // 初始化库存
        if (stock == UNINITIALIZED_STOCK) {
            RedisLock redisLock = new RedisLock(redisTemplate, key);
            try {
                // 获取锁
                if (redisLock.tryLock()) {
                    // 双重验证，避免并发时重复回源到数据库
                    stock = stock(key);
                    if (stock == UNINITIALIZED_STOCK) {
                        // 获取初始化库存
                        final int initStock = stockCallback.getStock();
                        // 将库存设置到redis
                        redisTemplate.opsForValue().set(key, initStock, expire, TimeUnit.SECONDS);
                        // 调一次扣库存的操作
                        stock = stock(key);
                    }
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            } finally {
                redisLock.unlock();
            }

        }
        return stock;
    }

    /**
     * 获取库存
     *
     * @param key 库存key
     * @return 0:库存不足; -1:库存未初始化; 大于0:剩余库存
     */
    public int getStock(String key) {
        Integer stock = (Integer) redisTemplate.opsForValue().get(key);
        return stock == null ? -1 : stock;
    }

    /**
     * 扣库存
     *
     * @param key 库存key
     * @return 扣减之前剩余的库存【0:库存不足; -1:库存未初始化; 大于0:扣减库存之前的剩余库存】
     */
    private Long stock(String key) {
        // 脚本里的KEYS参数
        List<String> keys = new ArrayList<>();
        keys.add(key);
        // 脚本里的ARGV参数
        List<String> args = new ArrayList<>();

        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // 集群模式
                if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection).eval(STOCK_LUA, keys, args);
                }

                // 单机模式
                else if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection).eval(STOCK_LUA, keys, args);
                }
                return UNINITIALIZED_STOCK;
            }
        });
        return result;
    }

}
