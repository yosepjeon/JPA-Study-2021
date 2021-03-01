package com.yosep.jpa.dataJpa.member.repository;

import com.yosep.jpa.dataJpa.member.data.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findOne(long id) {
        return em.find(User.class, id);
    }

}
