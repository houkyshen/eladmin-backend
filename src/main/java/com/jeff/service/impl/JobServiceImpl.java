package com.jeff.service.impl;

import com.jeff.domain.Job;
import com.jeff.repository.JobRepository;
import com.jeff.service.JobService;
import com.jeff.service.dto.JobQueryCriteria;
import com.jeff.service.mapstruct.JobMapper;
import com.jeff.utils.PageUtil;
import com.jeff.utils.QueryHelp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @Override
    public Map<String,Object> queryAll(JobQueryCriteria criteria, Pageable pageable) {
        Page<Job> page = jobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(jobMapper::toDto).getContent(),page.getTotalElements());
    }
}
