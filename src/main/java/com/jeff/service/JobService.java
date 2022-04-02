package com.jeff.service;

import com.jeff.service.dto.JobQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface JobService {


    /**
     * 分页查询
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Map<String,Object> queryAll(JobQueryCriteria criteria, Pageable pageable);

}
