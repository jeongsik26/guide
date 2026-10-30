package com.guide.ex.domain.post;

import com.guide.ex.domain.member.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Lombok으로 protected 기본 생성자를 추가
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(length = 100,nullable = false)
    @Builder.Default
    String title = ".";

    @Column(nullable = false, columnDefinition = "TEXT")
    @Builder.Default
    String content = ".";

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime registerDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @Column(precision = 12, scale = 8)
    private BigDecimal locationX;
    @Column(precision = 12, scale = 8)
    private BigDecimal locationY;

    @Column(nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Builder.Default
    private int views = 0;

    @Builder.Default
    private int commentCount = 0;

    private String postType;

    @Builder.Default
    private int likeCount = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Long getId() {
        return this.postId;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void changeDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}