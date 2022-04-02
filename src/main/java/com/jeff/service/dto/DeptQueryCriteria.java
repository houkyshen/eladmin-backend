package com.jeff.service.dto;

import com.jeff.annotation.DataPermission;
import com.jeff.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@DataPermission(fieldName = "id")
public class DeptQueryCriteria {

    @Query
    private Boolean enabled;

    @Query
    private Long pid;

    @Query(type = Query.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;

}
