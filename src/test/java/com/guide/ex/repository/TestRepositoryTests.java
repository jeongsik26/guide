package com.guide.ex.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class TestRepositoryTests {

    @Autowired
    private TestRepository testRepository;

    @Test
    public void testTime(){
        log.info("------------test time-------------");
        testRepository.getTime();
    }

    @Test
    @Transactional
    public void testInsert(){
        log.info("------------test testInsert-------------");

         com.guide.ex.domain.Test test = com.guide.ex.domain.Test.builder()
                 .testCol("testI").build();

        com.guide.ex.domain.Test result = testRepository.save(test);

        log.info("ID: " + result.getTestId());
        log.info("------------test testInsert-------------");

    }

    @Test
    public void testSelectAll(){
        log.info("------------test testSelectAll-------------");
        List<com.guide.ex.domain.Test> result = testRepository.findAll();
        log.info("ID: " +result);
    }

    @Test
    public void testSelectOne(){
        log.info("------------test testSelectOne-------------");
        Long id = testRepository.save(com.guide.ex.domain.Test.builder().testCol("select").build()).getTestId();
        Optional<com.guide.ex.domain.Test> result =  testRepository.findById(id);
        com.guide.ex.domain.Test test = result.orElseThrow();
        log.info(test.toString());
        log.info("------------test testSelectOne-------------");
    }

    @Test
    public void testDelete(){
        log.info("------------test testDelete-------------");
        testRepository.deleteById(2L);
    }
}
