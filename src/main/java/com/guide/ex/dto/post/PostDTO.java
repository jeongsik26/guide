package com.guide.ex.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private ImageDTO imageDTO;

    private Long postId;

    @NotNull
    private String title;

    @NotNull
    private String content;

    private LocalDateTime registerDate;
    private LocalDateTime modifyDate;

    @NotNull
    private BigDecimal locationX;

    @NotNull
    private BigDecimal locationY;

    private boolean isDeleted;

    private int views;

    private int likeCount;

    private int commentCount;

    private String postType;
    
    private Long memberId;
}
