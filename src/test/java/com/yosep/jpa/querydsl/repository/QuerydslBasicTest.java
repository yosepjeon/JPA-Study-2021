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
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @PersistenceContext
    EntityManager em;

    Logger logger = LoggerFactory.getLogger(QuerydslBasicTest.class);

    JPAQueryFactory queryFactory;
    QMember m;

    @BeforeEach
    public void before() {
        m = QMember.member;
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
    public void startJPQL() {
        // member1을 찾아라.
        String qlString = "select m from Member m " +
                "where m.userName = :userName";

        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("userName", "member1")
                .getSingleResult();

        Assertions.assertEquals(findMember.getUserName(), "member1");
    }

    @Test
    public void startQuerydsl() {
        // member1을 찾아라.
        Member findMember = queryFactory
                .select(m)
                .from(m)
                .where(m.userName.eq("member1"))
                .fetchOne();

        Assertions.assertEquals(findMember.getUserName(), "member1");
    }

    @Test
    public void getMemberListTest() {
        List<Member> members = queryFactory
                .selectFrom(m)
                .fetch();

        Assertions.assertEquals(members.size() > 0, true);
        logger.info("members: " + members);
    }

    @Test
    public void getFirstMemberTest() {
        Member findMember2 = queryFactory
                .selectFrom(m)
                .fetchFirst();

        logger.info("find first member: " + findMember2);
    }

    @Test
    public void getMembersForPaging() {
        QueryResults<Member> results = queryFactory
                .selectFrom(m)
                .fetchResults();

        logger.info("paging members: " + results);
    }

    @Test
    public void getMembersCount() {
        long count = queryFactory
                .selectFrom(m)
                .fetchCount();

        logger.info("member count: " + count);
    }

    @Test
    public void sortTest() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(m)
                .where(m.age.eq(100))
                .orderBy(m.age.desc(), m.userName.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        Assertions.assertEquals(member5.getUserName(), "member5");
        Assertions.assertEquals(member6.getUserName(), "member6");
        Assertions.assertEquals(memberNull.getUserName(), null);
    }
}
