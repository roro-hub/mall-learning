package com.roro.mall.tiny.service;

import com.roro.mall.tiny.common.api.CommonResult;

public interface UmsMemberService {

    CommonResult generateAuthCode(String telephone);

    CommonResult verifyAuthCode(String telephone, String authCode);
}
