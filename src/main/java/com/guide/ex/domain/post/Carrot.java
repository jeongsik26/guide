package com.guide.ex.domain.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor
@DiscriminatorValue("carrot")
public class Carrot extends Post{

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    @Builder.Default
    private boolean status = false;

    public void change(int price) {
        this.price = price;
    }

}