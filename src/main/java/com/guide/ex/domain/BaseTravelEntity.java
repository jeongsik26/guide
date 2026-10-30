package com.guide.ex.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = { AuditingEntityListener.class})
@Getter
abstract class BaseTravelEntity {

    @Column(name="start_travel_date")
    @NotNull
    private LocalDateTime startTravelDate;

    @Column(name = "end_travel_date")
    @NotNull
    private LocalDateTime endTravelDate;
}
