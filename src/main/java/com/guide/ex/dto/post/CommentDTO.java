package com.guide.ex.dto.post;

import com.guide.ex.dto.member.MemberDTO;
import com.guide.ex.dto.member.MemberProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long commentId;

    @NotEmpty
    private String commentContent;

    private LocalDateTime registerDate;

    private LocalDateTime modifyDate;

    @NotNull
    private Long postId;

    @NotNull
    private Long memberId;

    private MemberDTO member;
}
