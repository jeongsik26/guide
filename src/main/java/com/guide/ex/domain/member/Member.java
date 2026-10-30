package com.guide.ex.domain.member;

import com.guide.ex.domain.BaseEntity;
import com.guide.ex.domain.chat.ChatMessage;
import com.guide.ex.domain.chat.ChatRoom;
import com.guide.ex.domain.post.Comment;
import com.guide.ex.domain.post.Post;
import com.guide.ex.util.YearAttributeConverter;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"posts", "chatMessages", "sentChatRooms", "receivedChatRooms"})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(length = 100, nullable = false)
    private String uid;

    @Column(length = 256, nullable = false)
    private String pwd;

    @Column(length = 256, nullable = false)
    private String salt;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(nullable = false)
    @Convert(converter = YearAttributeConverter.class)
    private Year year;

    @Column(length = 10, nullable = false)
    private String gender;

    @Column(length = 100, nullable = false)
    private String name;

    // 자동으로 기본값을 설정하기 위해 rating 필드 수정
    @Column(length = 20, nullable = false, columnDefinition = "varchar(20) default '동메달'")
    private String rating;

    @Column(nullable = false)
    private boolean isBan;

    @Column(columnDefinition = "int default 0", name = "post_count")
    private int postCount;

    @Column(columnDefinition = "int default 0", name = "comment_count")
    private int commentCount;

    @Column(columnDefinition = "int default 0", name = "like_count")
    private int likeCount;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessages;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> sentChatRooms;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> receivedChatRooms;

//    private List<Comment> comments;

    // 새로운 회원이 추가될 때 rating 필드의 기본값 설정
    @PrePersist
    protected void onCreate() {
        if (this.rating == null) {
            this.rating = "동메달";
        }
    }

    public void change(String name) {
        this.name = name;
    }

    public Long getId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;

    }

//    public String getAgeRange() {
//        int currentYear = LocalDate.now().getYear(); // 현재 연도를 구함
//        int birthYear = this.year.getValue(); // Year 객체에서 연도를 int로 추출
//        int age = currentYear - birthYear; // 실제 나이 계산
//        int rangeStart = (age / 10) * 10; // 연령대 계산
//        return rangeStart + "대"; // "20대", "30대" 등의 문자열 반환
//    }

}
