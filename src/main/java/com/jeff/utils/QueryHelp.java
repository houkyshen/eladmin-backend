package com.jeff.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SuppressWarnings({"all"})
public class QueryHelp {

    //该方法对应于Specification的toPredicate方法，Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);
    //@root代表查询的实体类（表），可以通过root.join定义要关联查询的表；
    //@query代表查询条件，具体指的是哪些字段需要条件查询，如这里使用的是自定义的查询条件类UserQueryCriteria；
    //@cb代表构建查询条件，如or, and ,like, equal等等
    //JPQL：select user from User user left join Dept dept where user.dept.id in (2)
    public static <R, Q> Predicate getPredicate(Root<R> root, Q query, CriteriaBuilder cb) {
        //条件的集合
        List<Predicate> list = new ArrayList<>();
        if (query == null) {
            return cb.and(list.toArray(new Predicate[0]));
        }
        // 获取数据权限，就是部门id的集合
        List<Long> dataScopes = SecurityUtils.getCurrentUserDataScope();
        if (CollectionUtil.isNotEmpty(dataScopes)) {
            //root代表User，这里的join代表Dept
            Join join = root.join("dept", JoinType.LEFT);
            //添加条件：用户的部门id要在dataScopes里面，dataScopes指当前登录用户能看到的部门id集合
            list.add(join.get("id").in(dataScopes));
        }
        return cb.and(list.toArray(new Predicate[0]));
    }
}
