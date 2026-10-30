package com.guide.ex.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = { AuditingEntityListener.class})
@Getter
public abstract class BaseEntity {

    //생성시 시간 정보 현재시간으로 저장     
    @CreatedDate
    @Column(name="regdate", updatable = false)
    private LocalDateTime regDate;

    //마지막 수정시간 저장
    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;
}
