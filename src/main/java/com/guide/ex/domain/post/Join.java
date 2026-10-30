package com.guide.ex.domain.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Table(name = "postJoin")
@DiscriminatorValue("postJoin")

public class Join extends Post {

    @Column(nullable = false)
    private int numPeople;

    @Column(nullable = false)
    private int expense;

    private LocalDate startTravelDate;

    private LocalDate endTravelDate;

    public void change(int expense, int numPeople, LocalDate startTravelDate, LocalDate endTravelDate) {
        this.expense = expense;
        this.numPeople = numPeople;
        this.startTravelDate = startTravelDate;
        this.endTravelDate = endTravelDate;
    }
}