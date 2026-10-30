package com.guide.ex.domain.post;

import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@SuperBuilder
@Entity
@Getter
@NoArgsConstructor
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;       // 기본키로 사용

    @Column(name="uuid", length = 500)
    private String uuid;

    @Column(name = "file_name", length = 200)
    private String fileName;

    @Column(name = "ord", length = 50)
    @Builder.Default
    private int ord = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")       // Post테이블의 외래키로 받아옴
    private Post post;

    // 순환 참조 방지
    @Override
    public String toString() {
        return "PostImage{" +
                "id=" + imageId +
                ", uuid='" + uuid + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
    public void setPost(Post post) {
        this.post = post;
    }

    public Long getImageId() {
        return this.imageId;
    }
}