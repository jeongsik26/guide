package com.guide.ex.dto.post;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PostListCommentCountDTO {
    private Long postId;
    private String title;
    private int content;
    private LocalDateTime registerDate;
    private LocalDateTime modifyDate;
    private BigDecimal locationX;
    private BigDecimal locationY;
    private boolean isDeleted;
    private int views;
    private int likeCount;
    private String postType;
    private Long memberId;

    // comment 테이블로부터 가져온다
    private Long commentCnt;
}
