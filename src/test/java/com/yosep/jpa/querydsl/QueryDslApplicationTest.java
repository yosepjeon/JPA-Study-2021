package com.yosep.jpa.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.jpa.querydsl.data.entity.Hello;
import com.yosep.jpa.querydsl.data.entity.QHello;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class QueryDslApplicationTest {

    @Autowired
    EntityManager em;

    @Test
    void contextLoads() {
        Hello hello = new Hello();
        em.persist(hello);

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        QHello qHello = QHello.hello;

        Hello result= jpaQueryFactory.selectFrom(qHello)
                .fetchOne();

        Assertions.assertEquals(hello, result);
        Assertions.assertEquals(hello.getId(), result.getId());
    }
}
