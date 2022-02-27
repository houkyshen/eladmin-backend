package com.jeff.service;

import com.jeff.service.dto.UserDto;
import com.jeff.service.dto.small.RoleSmallDto;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface RoleService {
    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return /
     */
    List<RoleSmallDto> findByUsersId(Long id);

    /**
     * 获取用户权限信息
     * @param user 用户信息
     * @return 权限信息
     */
    List<GrantedAuthority> mapToGrantedAuthorities(UserDto user);
}
