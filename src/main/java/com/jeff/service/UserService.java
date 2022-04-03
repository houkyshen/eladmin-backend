package com.jeff.service;

import com.jeff.domain.User;
import com.jeff.service.dto.UserDto;
import com.jeff.service.dto.UserQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface UserService {

    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    UserDto findById(long id);

    /**
     * 根据用户名查询
     * @param userName /
     * @return /
     */
    UserDto findByName(String userName);

    Object queryAll(UserQueryCriteria userQueryCriteria, Pageable pageable);

    /**
     * 新增用户
     * @param resources /
     */
    void create(User resources);

    /**
     * 修改用户
     * @param resources /
     * @throws Exception /
     */
    void update(User resources) throws Exception;

    /**
     * 删除用户
     * @param ids /
     */
    void delete(Set<Long> ids);
}
