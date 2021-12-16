package com.jeff.service.impl;

import com.jeff.domain.User;
import com.jeff.repository.UserRepository;
import com.jeff.service.UserService;
import com.jeff.service.dto.UserDto;
import com.jeff.service.mapstruct.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDto findByName(String userName) {
        User user = userRepository.findByUsername(userName);
        return userMapper.toDto(user);
    }

}
