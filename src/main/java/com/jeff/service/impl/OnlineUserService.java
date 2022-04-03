package com.jeff.service.impl;

import com.jeff.config.SecurityProperties;
import com.jeff.service.dto.JwtUserDto;
import com.jeff.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class OnlineUserService {

    private final SecurityProperties properties;
    private final RedisUtils redisUtils;

    public OnlineUserService(SecurityProperties properties, RedisUtils redisUtils) {
        this.properties = properties;
        this.redisUtils = redisUtils;
    }

    /**
     * 保存在线用户信息
     *
     * @param jwtUserDto /
     * @param token      /
     * @param request    /
     */
    public void save(JwtUserDto jwtUserDto, String token, HttpServletRequest request) {
        redisUtils.set(properties.getOnlineKey() + token, jwtUserDto, properties.getTokenValidityInSeconds() / 1000);
    }

    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */
    public JwtUserDto getOne(String key) {
        return (JwtUserDto) redisUtils.get(key);
    }
}
