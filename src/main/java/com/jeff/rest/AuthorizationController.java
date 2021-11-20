package com.jeff.rest;

import cn.hutool.core.util.IdUtil;
import com.jeff.service.dto.AuthUserDto;
import com.jeff.utils.RedisUtils;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final RedisUtils redisUtils;

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUserDto authUser, HttpServletRequest request) throws Exception {
        // 查询Redis中的验证码
        String code = (String) redisUtils.get(authUser.getUuid());
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new Exception("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new Exception("验证码错误");
        }
        //走到这里就代表验证码校验通过了
        return ResponseEntity.ok("验证码校验通过，但是还没有进行认证和授权");
    }

    @GetMapping(value = "/code")
    public ResponseEntity<Object> getCode() {
        // 获取运算的结果
        Captcha captcha = new ArithmeticCaptcha(111, 36);
        captcha.setLen(2);
        String uuid = "code-key-" + IdUtil.simpleUUID();
        String captchaValue = captcha.text();
        // 保存
        redisUtils.set(uuid, captchaValue, 2L, TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }
}
