package com.yosep.jpa.querydsl.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.jpa.querydsl.data.entity.Member;
import com.yosep.jpa.querydsl.data.entity.QMember;
import com.yosep.jpa.querydsl.data.entity.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
public class QuerydslPagingTest {
    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;
    QMember member;

    @BeforeEach
    public void before() {
        member = QMember.member;
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void paging1() {
        List<Member> members = queryFactory
                .selectFrom(member)
                .orderBy(member.userName.desc())
                .offset(1)  // 0부터 시작(zero index)
                .limit(2)   // 최대 2건 조회
                .fetch();

        Assertions.assertEquals(members.size(), 2);
    }

    @Test
    public void paging2() {
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.userName.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        Assertions.assertEquals(queryResults.getTotal(), 4);
        Assertions.assertEquals(queryResults.getLimit(), 2);
        Assertions.assertEquals(queryResults.getOffset(), 1);
        Assertions.assertEquals(queryResults.getResults().size(), 2);
    }
}
