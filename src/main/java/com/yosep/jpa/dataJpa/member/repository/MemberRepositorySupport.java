package com.yosep.jpa.dataJpa.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.jpa.dataJpa.member.data.entity.Member;
import com.yosep.jpa.dataJpa.member.data.entity.QMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    QMember member= QMember.member;


    @Autowired
    public MemberRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Member.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Member> findAll() {

        return jpaQueryFactory
                .selectFrom(member)
                .fetch();
    }

    public List<com.yosep.jpa.dataJpa.member.data.entity.Member> findByName(String name) {

        return jpaQueryFactory
                .selectFrom(member)
                .where(member.name.eq(name))
                .fetch();
    }
}
