package com.yosep.jpa.dataJpa.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.jpa.dataJpa.member.data.entity.QUser;
import com.yosep.jpa.dataJpa.member.data.entity.User;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    QUser member= QUser.user;


    public MemberRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(User.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<User> findAll() {

        return jpaQueryFactory
                .selectFrom(member)
                .fetch();
    }

    public List<User> findByName(String name) {

        return jpaQueryFactory
                .selectFrom(member)
                .where(member.name.eq(name))
                .fetch();
    }
}
