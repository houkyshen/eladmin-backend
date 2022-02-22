package com.jeff.service.mapstruct;

import com.jeff.base.BaseMapper;
import com.jeff.domain.Menu;
import com.jeff.service.dto.MenuDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper extends BaseMapper<MenuDto, Menu> {
}
