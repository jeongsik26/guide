package com.guide.ex.domain.post;

import com.guide.ex.domain.member.Member;
import com.guide.ex.domain.member.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "Comment", indexes = {
        @Index(name = "idx_comment_post_id", columnList = "post_id"),
        @Index(name = "idx_comment_member_id", columnList = "member_id"),
        @Index(name = "idx_comment_post_member", columnList = "post_id, member_id")  // 복합 인덱스
})
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "post")
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Comment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false, length = 200, name = "comment_content")
    private String commentContent;

    @CreatedDate
    @Column(nullable = true)
    private LocalDateTime registerDate;

    @LastModifiedDate
    @Column(nullable = true)
    private LocalDateTime modifyDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    public void change(String commentContent) {
        this.commentContent = commentContent;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
