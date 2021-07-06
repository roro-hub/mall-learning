package com.roro.mall.tiny.service;

import com.github.pagehelper.util.StringUtil;
import com.roro.mall.tiny.common.api.CommonResult;
import com.roro.mall.tiny.service.RedisService;
import com.roro.mall.tiny.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UmsMemberServiceImpl implements UmsMemberService {

    @Autowired
    private RedisService redisService;

    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.key.expire.authCode}")
    private Long REDIS_KEY_EXPIRE_SECONDS;

    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString(), REDIS_KEY_EXPIRE_SECONDS, TimeUnit.SECONDS);
        return CommonResult.success(REDIS_KEY_PREFIX_AUTH_CODE + telephone+ "_" + sb.toString(), "获取验证码成功");
    }

    @Override
    public CommonResult verifyAuthCode(String telephone, String authCode) {
        if (StringUtil.isEmpty(authCode)) {
            return CommonResult.failed("请输入验证码");
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        boolean result = authCode.equals(realAuthCode);
        if (result) {
            // 校验成功后删除key
            redisService.remove(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
            return CommonResult.success(null, "验证码校验成功");
        }
        return CommonResult.failed("验证码不正确");
    }
}
