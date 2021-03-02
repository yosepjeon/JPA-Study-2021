package com.yosep.jpa.querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.jpa.querydsl.data.entity.Member;
import com.yosep.jpa.querydsl.data.entity.QMember;
import com.yosep.jpa.querydsl.data.entity.QTeam;
import com.yosep.jpa.querydsl.data.entity.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
public class QuerydslSubQueryTest {
    @PersistenceContext
    EntityManager em;
    @PersistenceUnit
    EntityManagerFactory emf;

    JPAQueryFactory queryFactory;
    QMember member;
    QTeam team;

    @BeforeEach
    public void before() {
        member = QMember.member;
        team = QTeam.team;

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
    public void subQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");

//        List<Member> result = queryFactory
//                .selectFrom(member)
//                .where(
//                        member.age.eq(
//                                JPAExpressions
//                                        .select(memberSub.age.max())
//                                        .from(memberSub)
//                        ))
//                .fetch();

//                Assertions.assertEquals(result.get(0).getAge(), 40);

        Member result = queryFactory
                .selectFrom(member)
                .where(
                        member.age.eq(
                                JPAExpressions
                                        .select(memberSub.age.max())
                                        .from(memberSub)
                        ))
                .fetchOne();

        Assertions.assertEquals(result.getAge(), 40);
        System.out.println("subQuery#" + result);
    }

    @Test
    public void subQueryGoe() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        System.out.println("goe#" + result);
        for (Member member : result) {
            Assertions.assertEquals(member.getAge() >= 25, true);
        }
    }

    @Test
    public void subQueryIn() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        System.out.println("in#" + result);
        for (Member member : result) {
            Assertions.assertEquals(member.getAge() > 10, true);
        }
    }

    @Test
    public void subQuerySelect() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Tuple> fetch = queryFactory
                .select(member.userName,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ).from(member)
                .fetch();

        for(Tuple tuple : fetch) {
            System.out.println("username = " + tuple.get(member.userName));
            System.out.println("age = " + tuple.get(JPAExpressions.select(memberSub.age.avg())
            .from(memberSub)));
        }
    }
}
