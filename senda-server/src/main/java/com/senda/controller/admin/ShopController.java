package com.senda.controller.admin;

import com.senda.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("adminShopController")
@RequestMapping("/admin/shop")
public class ShopController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置营业状态
     * @param status
     * @return
     */
    @PutMapping("{status}")
    public Result<String> setStatus(@PathVariable Integer status) {
        log.info("设置营业状态:{}", status);
        redisTemplate.opsForValue().set("SHOP_STATUS", status);
        return Result.success(status.toString());
    }

    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        log.info("获取营业状态:{}", status);
        return Result.success(status);
    }

}
