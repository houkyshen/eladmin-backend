package com.jeff.service.dto;

import com.jeff.annotation.Query;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobQueryCriteria {

    @Query
    private Boolean enabled;

}
