package com.yosep.jpa.dataJpa.member.service;

import com.yosep.jpa.dataJpa.member.data.entity.User;
import com.yosep.jpa.dataJpa.member.repository.MemberRepository;
import com.yosep.jpa.dataJpa.member.repository.MemberRepositorySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRepositorySupport memberRepositorySupport;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberRepositorySupport memberRepositorySupport) {
        this.memberRepository = memberRepository;
        this.memberRepositorySupport = memberRepositorySupport;
    }

    @Transactional
    public long join(User user) {
        validateDuplicateMember(user);
        memberRepository.save(user);

        return user.getId();
    }

    private void validateDuplicateMember(User user) {
        List<User> findUsers =
                memberRepositorySupport.findByName(user.getName());

        if(!findUsers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<User> findMembers() {
        return memberRepositorySupport.findAll();
    }

    public User findOne(long memberId) {
        return memberRepository.findOne(memberId);
    }
}
