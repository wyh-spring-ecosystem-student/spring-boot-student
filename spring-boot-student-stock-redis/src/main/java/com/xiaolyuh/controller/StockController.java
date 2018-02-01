package com.xiaolyuh.controller;

import com.xiaolyuh.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuhao.wang
 */
@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "stock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object stock() {
        // 商品ID
        long commodityId = 1;
        // 库存ID
        String redisKey = "redis_key:stock:" + commodityId;
        long stock = stockService.stock(redisKey, 60 * 60, 2, () -> initStock(commodityId));
        return stock >= 0;
    }

    /**
     * 获取初始的库存
     *
     * @return
     */
    private int initStock(long commodityId) {
        // TODO 这里做一些初始化库存的操作
        return 1000;
    }

    @RequestMapping(value = "getStock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object getStock() {
        // 商品ID
        long commodityId = 1;
        // 库存ID
        String redisKey = "redis_key:stock:" + commodityId;

        return stockService.getStock(redisKey);
    }

    @RequestMapping(value = "addStock", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object addStock() {
        // 商品ID
        long commodityId = 2;
        // 库存ID
        String redisKey = "redis_key:stock:" + commodityId;

        return stockService.addStock(redisKey, 2);
    }
}