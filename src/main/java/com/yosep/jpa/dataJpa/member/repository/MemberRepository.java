package com.yosep.jpa.dataJpa.member.repository;

import com.yosep.jpa.dataJpa.member.data.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public void save(Member user) {
        em.persist(user);
    }

    public Member findOne(long id) {
        return em.find(Member.class, id);
    }

}
