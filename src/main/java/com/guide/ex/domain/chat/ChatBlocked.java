package com.guide.ex.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatBlocked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blocked_id")
    private Long blockedID;

    @NotNull
    @Column(name = "blocked_user")
    private String blockedUser;

    @NotNull
    @CreatedDate
    @Column(name = "regist_date")
    private LocalDateTime registDate;

    public void change(String s, String s1) {

    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;
}
