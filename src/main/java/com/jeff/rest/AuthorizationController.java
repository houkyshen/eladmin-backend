package com.jeff.rest;

import cn.hutool.core.util.IdUtil;
import com.jeff.config.RsaProperties;
import com.jeff.config.SecurityProperties;
import com.jeff.config.jwt.TokenProvider;
import com.jeff.service.dto.AuthUserDto;
import com.jeff.service.dto.JwtUserDto;
import com.jeff.service.impl.OnlineUserService;
import com.jeff.utils.RedisUtils;
import com.jeff.utils.RsaUtils;
import com.jeff.utils.SecurityUtils;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityProperties properties;
    private final TokenProvider tokenProvider;
    private final OnlineUserService onlineUserService;

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUserDto authUser, HttpServletRequest request) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
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
        //认证授权
        //1、根据前端传来的用户名和密码构造一个UsernamePasswordAuthenticationToken实例
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        //2、认证UsernamePasswordAuthenticationToken实例，认证成功则返回包含用户信息的Authentication实例
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //3、设置当前登录用户，这一步是为了可以让其他类或方法通过SecurityContextHolder.getContext().getAuthentication()拿到当前登录的用户
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //4、通过已经认证的Authentication返回UserDetails
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        // 生成令牌
        String token = tokenProvider.createToken(authentication);
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        //走到这里就代表验证码校验通过了
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUserDto);
        }};
        return ResponseEntity.ok(authInfo);
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity<Object> getUserInfo() {
        return ResponseEntity.ok(SecurityUtils.getCurrentUser());
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
