package com.jeff.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeff.base.BaseDTO;
import com.jeff.service.dto.small.DeptSmallDto;
import com.jeff.service.dto.small.JobSmallDto;
import com.jeff.service.dto.small.RoleSmallDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class UserDto extends BaseDTO implements Serializable {

    private Long id;

    private Set<RoleSmallDto> roles;

    private Set<JobSmallDto> jobs;

    private DeptSmallDto dept;

    private Long deptId;

    private String username;

    private String nickName;

    private String email;

    private String phone;

    private String gender;

    private String avatarName;

    private String avatarPath;

    @JsonIgnore
    private String password;

    private Boolean enabled;

    @JsonIgnore
    private Boolean isAdmin = false;

    private Date pwdResetTime;
}
