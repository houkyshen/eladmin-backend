package com.jeff.service.impl;

import com.jeff.domain.User;
import com.jeff.repository.UserRepository;
import com.jeff.service.UserService;
import com.jeff.service.dto.UserDto;
import com.jeff.service.mapstruct.UserMapper;
import com.jeff.utils.PageUtil;
import com.jeff.utils.QueryHelp;
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
        //这里findall方法的第一个参数是Specification，Specification是一个函数式接口，因为他只有一个抽象方法:toPredicate()，所以可以用lambda表达式去表示函数式接口
        Page<User> page = userRepository.findAll(QueryHelp::getPredicate, pageable);
        //以上代码等价于以下代码：
//        Page<User> page = userRepository.findAll(new Specification<User>() {
//            @Override
//            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                return QueryHelp.getPredicate(root, query, criteriaBuilder);
//            }
//        }, pageable);
        return PageUtil.toPage(page.map(userMapper::toDto));
    }

    @Override
    public UserDto findByName(String userName) {
        User user = userRepository.findByUsername(userName);
        return userMapper.toDto(user);
    }

}
