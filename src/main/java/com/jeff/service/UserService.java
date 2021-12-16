package com.jeff.service;

import com.jeff.service.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    /**
     * 根据用户名查询
     * @param userName /
     * @return /
     */
    UserDto findByName(String userName);
}
