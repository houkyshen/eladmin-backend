package com.jeff.service.mapstruct;

import com.jeff.base.BaseMapper;
import com.jeff.domain.Dept;
import com.jeff.service.dto.DeptDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptMapper extends BaseMapper<DeptDto, Dept> {
}
