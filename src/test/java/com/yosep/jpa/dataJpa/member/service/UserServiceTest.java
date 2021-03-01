package com.yosep.jpa.dataJpa.member.service;

import com.yosep.jpa.dataJpa.member.data.entity.User;
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
        User user = new User();
        user.setName("kim");

        // When
        long saveId = memberService.join(user);

        // Then
        Assertions.assertEquals(user, memberRepository.findOne(saveId));
    }

    @Test
    public void reduplicateMemberExceptionTest() {
        // Given
        User member1 = new User();
        member1.setName("kim");

        User member2 = new User();
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
