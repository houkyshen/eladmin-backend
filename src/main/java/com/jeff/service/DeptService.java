package com.jeff.service;


import com.jeff.domain.Dept;
import com.jeff.service.dto.DeptDto;
import com.jeff.service.dto.DeptQueryCriteria;

import java.util.List;
import java.util.Set;

public interface DeptService {
    /**
     * 查询所有数据
     *
     * @param criteria 条件
     * @param isQuery  /
     * @return /
     * @throws Exception /
     */
    List<DeptDto> queryAll(DeptQueryCriteria criteria, Boolean isQuery) throws Exception;

    /**
     * 根据PID查询
     *
     * @param pid /
     * @return /
     */
    List<Dept> findByPid(long pid);

    /**
     * 根据角色ID查询
     *
     * @param id /
     * @return /
     */
    Set<Dept> findByRoleId(Long id);

    /**
     * 获取下级部门
     *
     * @param deptList
     * @return
     */
    List<Long> getDeptChildren(List<Dept> deptList);
}
