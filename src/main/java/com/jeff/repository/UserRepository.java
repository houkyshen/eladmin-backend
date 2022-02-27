package com.jeff.repository;

import com.jeff.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
//<User, Long>前者代表要查哪个表，后者代表id的类型
//继承JpaRepository之后，会默认使用SimpleJpaRepository这个实现类作为UserRepository接口的实体bean
//继承JpaSpecificationExecutor是为了实现动态查询
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);
}
