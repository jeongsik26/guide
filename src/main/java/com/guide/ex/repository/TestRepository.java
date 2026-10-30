package com.guide.ex.repository;

import com.guide.ex.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TestRepository  extends JpaRepository<Test, Long> {

    @Query(value = "select now()", nativeQuery = true)
    java.time.LocalDateTime getTime();
}
