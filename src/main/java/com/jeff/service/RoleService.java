package com.jeff.service;

import com.jeff.service.dto.small.RoleSmallDto;

import java.util.List;

public interface RoleService {
    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return /
     */
    List<RoleSmallDto> findByUsersId(Long id);
}
