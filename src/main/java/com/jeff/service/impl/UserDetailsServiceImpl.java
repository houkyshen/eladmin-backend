package com.jeff.service.impl;

import com.jeff.service.RoleService;
import com.jeff.service.UserService;
import com.jeff.service.dto.JwtUserDto;
import com.jeff.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = userService.findByName(username);
        JwtUserDto jwtUserDto = new JwtUserDto(
                user,
                null,
                //获取当前用户的权限，并进行授权
                roleService.mapToGrantedAuthorities(user)
        );
        return jwtUserDto;
    }
}
