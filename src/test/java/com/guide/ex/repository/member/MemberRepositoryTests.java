package com.guide.ex.repository.member;

import com.guide.ex.repository.member.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.guide.ex.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void memberInsertTest() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Member member = Member.builder()
                    .name("kim")
                    .uid("travel"+i)
                    .pwd("maker"+i)
                    .salt("someSaltValue") // 여기에 적절한 salt 값을 추가
                    .phone("01012345678")
                    .year(Year.of(1999))
                    .gender("male")
                    .build();

            Member result = memberRepository.save(member);
            log.info("member_id: " + result.getMemberId());
        });
    }

    @Test
    public void memberSelectTest() {
        Long member_id = 1L;

        Optional<Member> result = memberRepository.findById(member_id);

        Member member = result.orElseThrow();

        log.info(member);
    }

    @Test
    public void memberUpdateTest() {
        Long member_id = memberRepository.save(Member.builder()
                .name("tester")
                .uid("tester" + System.nanoTime())
                .pwd("pwd")
                .salt("")
                .phone("01012345678")
                .year(java.time.Year.of(1999))
                .gender("male")
                .build()).getMemberId();

        Optional<Member> result = memberRepository.findById(member_id);

        Member member = result.orElseThrow();

//        member.change("update 이채림");

        memberRepository.save(member);
    }

    @Test
    public void memberDeleteTest() {
        Long member_id = 57L;

        memberRepository.deleteById(member_id);
    }

    @Test
    public void memberPagingTest() {
        Pageable pageable = PageRequest.of(0, 6, Sort.by("memberId"));

        Page<Member> result = memberRepository.findAll(pageable);

        log.info("total count: "+result.getTotalElements());
        log.info( "total pages:" +result.getTotalPages());
        log.info("page number: "+result.getNumber());
        log.info("page size: "+result.getSize());

        List<Member> members = result.getContent();

        members.forEach(member -> log.info(member));
    }
}
