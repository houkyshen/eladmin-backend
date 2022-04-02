package com.jeff.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jeff.annotation.DataPermission;
import com.jeff.annotation.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@SuppressWarnings({"all"})
public class QueryHelp {

    //该方法对应于Specification的toPredicate方法，Predicate toPredicate(Root<T> root, CriteriaQuery<?> queryCriteria, CriteriaBuilder criteriaBuilder);
    //@root代表查询的实体类（表），可以通过root.join定义要关联查询的表；
    //@query代表查询条件，具体指的是哪些字段需要条件查询，如这里使用的是自定义的查询条件类UserQueryCriteria；
    //@cb代表构建查询条件，如or, and ,like, equal等等
    //JPQL：select user from User user left join Dept dept where user.dept.id in (2)
    public static <R, Q> Predicate getPredicate(Root<R> root, Q queryCriteria, CriteriaBuilder cb) {
        //条件的集合
        List<Predicate> list = new ArrayList<>();
        if (queryCriteria == null) {
            return cb.and(list.toArray(new Predicate[0]));
        }
        // 获取数据权限，就是部门id的集合
//        List<Long> dataScopes = SecurityUtils.getCurrentUserDataScope();
//        if (CollectionUtil.isNotEmpty(dataScopes)) {
//            //root代表User，这里的join代表Dept
//            Join join = root.join("dept", JoinType.LEFT);
//            //添加条件：用户的部门id要在dataScopes里面，dataScopes指当前登录用户能看到的部门id集合
//            list.add(join.get("id").in(dataScopes));
//        }

        // 数据权限验证
        DataPermission permission = queryCriteria.getClass().getAnnotation(DataPermission.class);
        if (permission != null) {
            // 获取数据权限
            List<Long> dataScopes = SecurityUtils.getCurrentUserDataScope();
            if (CollectionUtil.isNotEmpty(dataScopes)) {
                if (StringUtils.isNotBlank(permission.joinName()) && StringUtils.isNotBlank(permission.fieldName())) {
                    Join join = root.join(permission.joinName(), JoinType.LEFT);
                    list.add(join.get("id").in(dataScopes));
                } else if (StringUtils.isBlank(permission.joinName()) && StringUtils.isNotBlank(permission.fieldName())) {
                    list.add(root.get("id").in(dataScopes));
                }
            }
        }

        try {
            //获取UserQueryCriteria类的所有字段
            List<Field> fields = Arrays.asList(queryCriteria.getClass().getDeclaredFields());
            //遍历每个字段
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                // 设置对象的访问权限，保证对private的属性的访
                field.setAccessible(true);
                Query q = field.getAnnotation(Query.class);
                if (q != null) {
                    String propName = q.propName();
                    String joinName = q.joinName();
                    String blurry = q.blurry();
                    //用作条件查询的字段名，如id，
                    String attributeName = isBlank(propName) ? field.getName() : propName;
                    Class<?> fieldType = field.getType();
                    //前端传来的条件值
                    Object valueFromFrontEnd = field.get(queryCriteria);
                    if (ObjectUtil.isNull(valueFromFrontEnd) || "".equals(valueFromFrontEnd)) {
                        continue;
                    }
                    Join join = null;
                    //模糊多字段
                    if (ObjectUtil.isNotEmpty(blurry)) {
                        String[] blurrys = blurry.split(",");
                        List<Predicate> orPredicate = new ArrayList<>();
                        for (String s : blurrys) {
                            orPredicate.add(cb.like(root.get(s), "%" + valueFromFrontEnd.toString() + "%"));
                        }
                        list.add(cb.or(orPredicate.toArray(new Predicate[0])));
                        continue;
                    }
                    if (ObjectUtil.isNotEmpty(joinName)) {
                        String[] joinNames = joinName.split(">");
                        for (String name : joinNames) {
                            switch (q.join()) {//这里只用到左连接，所以就只写左连接的情况
                                case LEFT:
                                    if (ObjectUtil.isNotNull(join) && ObjectUtil.isNotNull(valueFromFrontEnd)) {
                                        join = join.join(name, JoinType.LEFT);
                                    } else {
                                        join = root.join(name, JoinType.LEFT);
                                    }
                                default:
                                    break;
                            }
                        }
                    }
                    switch (q.type()) {
                        case EQUAL:
                            list.add(cb.equal(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), valueFromFrontEnd));
                            break;
                        case IS_NULL:
                            list.add(cb.isNull(getExpression(attributeName, join, root)));
                            break;
                        case GREATER_THAN:
                            list.add(cb.greaterThan(getExpression(attributeName, join, root)
                                    , (Comparable) valueFromFrontEnd));
                            break;
                        case IN:
                            if (CollUtil.isNotEmpty((Collection<Object>) valueFromFrontEnd)) {
                                list.add(getExpression(attributeName, join, root).in((Collection<Object>) valueFromFrontEnd));
                            }
                            break;
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return cb.and(list.toArray(new Predicate[0]));
    }

    private static <T, R> Expression<T> getExpression(String attributeName, Join join, Root<R> root) {
        if (ObjectUtil.isNotEmpty(join)) {
            return join.get(attributeName);
        } else {
            return root.get(attributeName);
        }
    }
}
