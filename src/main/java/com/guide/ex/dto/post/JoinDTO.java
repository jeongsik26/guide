package com.guide.ex.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class JoinDTO extends PostDTO{

    private Long postId;

    private int expense;

    private int numPeople;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startTravelDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endTravelDate;

    private List<ImageDTO> imageDTOs;

    public List<ImageDTO> getImageDTOs() {
        return imageDTOs;
    }

    // 이미지 리스트 설정 메서드
    public void setImageDTOs(List<ImageDTO> imageDTOs) {
        this.imageDTOs = imageDTOs;
    }
}
