package com.jeff.service.mapstruct;

import com.jeff.base.BaseMapper;
import com.jeff.domain.User;
import com.jeff.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserDto, User> {
}
