package com.yosep.jpa.dataJpa.member.service;

import com.yosep.jpa.dataJpa.member.data.entity.Member;
import com.yosep.jpa.dataJpa.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void joinTest() throws Exception {
        // Given
        Member user = new Member();
        user.setName("kim");

        // When
        long saveId = memberService.join(user);

        // Then
        Assertions.assertEquals(user, memberRepository.findOne(saveId));
    }

    @Test
    public void reduplicateMemberExceptionTest() {
        // Given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // When
        try {
            memberService.join(member1);
            memberService.join(member2);
        }catch (IllegalStateException e) {
            System.out.println("exception: " + e.getMessage());
        }
    }

}
