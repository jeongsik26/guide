package com.guide.ex.dto.member;

import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import java.time.Year;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileDTO {
    private Long memberProfileId;
    private String content;
    private String fileName;
    private String uuid;
    @NotEmpty
    private String travelType;

    private Long memberId;
}
