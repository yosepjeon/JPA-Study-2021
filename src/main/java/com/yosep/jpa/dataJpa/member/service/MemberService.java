package com.yosep.jpa.dataJpa.member.service;

import com.yosep.jpa.dataJpa.member.data.entity.Member;
import com.yosep.jpa.dataJpa.member.repository.MemberRepository;
import com.yosep.jpa.dataJpa.member.repository.MemberRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRepositorySupport memberRepositorySupport;

    @Transactional
    public long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findUsers =
                memberRepositorySupport.findByName(member.getName());

        if (!findUsers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findMembers() {
        return memberRepositorySupport.findAll();
    }

    public Member findOne(long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
