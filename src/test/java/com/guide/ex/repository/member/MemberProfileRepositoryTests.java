package com.guide.ex.repository.member;

import com.guide.ex.domain.member.Member;
import com.guide.ex.domain.member.MemberProfile;
import com.guide.ex.repository.member.MemberProfileRepository;
import com.guide.ex.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class MemberProfileRepositoryTests {

    @Autowired
    private MemberProfileRepository memberProfileRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void memberProfileInsertTest() {
        Member member = memberRepository.save(Member.builder()
                .name("tester")
                .uid("tester" + System.nanoTime())
                .pwd("pwd")
                .salt("")
                .phone("01012345678")
                .year(java.time.Year.of(1999))
                .gender("male")
                .build());
        MemberProfile memberProfile = MemberProfile.builder()
                .member(member)
                .travelType("ENFP")
                .fileName("exfileName.jpg")
                .content("자기소개 ..................")
                .uuid("asdkjfh3h9e8uehw")
                .build();

        memberProfileRepository.save(memberProfile);
    }

    @Test
    @Transactional
    public void memberProfileSelectOneTest() {
        Member member = Member.builder().memberId(1L).build();
        Optional<MemberProfile> memberProfile = memberProfileRepository.findByMember(member);
        log.info(memberProfile.toString());
    }

}
