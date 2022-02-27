package com.jeff.service.impl;

import com.jeff.domain.User;
import com.jeff.repository.UserRepository;
import com.jeff.service.UserService;
import com.jeff.service.dto.UserDto;
import com.jeff.service.mapstruct.UserMapper;
import com.jeff.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;

    @Override
    public Object queryAll(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return PageUtil.toPage(page.map(userMapper::toDto));
    }

    @Override
    public UserDto findByName(String userName) {
        User user = userRepository.findByUsername(userName);
        return userMapper.toDto(user);
    }

}
