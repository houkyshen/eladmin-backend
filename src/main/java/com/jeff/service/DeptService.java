package com.jeff.service;


import com.jeff.domain.Dept;

import java.util.List;
import java.util.Set;

public interface DeptService {

    /**
     * 根据PID查询
     * @param pid /
     * @return /
     */
    List<Dept> findByPid(long pid);

    /**
     * 根据角色ID查询
     * @param id /
     * @return /
     */
    Set<Dept> findByRoleId(Long id);

    /**
     * 获取下级部门
     * @param deptList
     * @return
     */
    List<Long> getDeptChildren(List<Dept> deptList);
}
