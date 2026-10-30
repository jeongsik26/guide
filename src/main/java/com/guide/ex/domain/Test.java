package com.guide.ex.domain;


import lombok.*;

import jakarta.persistence.*;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    @Column(length = 500, nullable = false)
    private String testCol;

}
