package com.jeff.service;

import com.jeff.service.dto.JwtUserDto;
import com.jeff.service.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.equals("admin")) throw new UsernameNotFoundException("");
        UserDto user = new UserDto();
        user.setEnabled(true);
        user.setNickName("Jeff");
        user.setPassword("$2a$10$.a.DezMXYcnDOKxlL7JKDuRxt9VrZLyJHdjGHklNY6mo3FcTRY/GW");
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("Admin");
        JwtUserDto jwtUserDto = new JwtUserDto(
                user,
                null,
                authorities
        );
        return jwtUserDto;
    }
}
