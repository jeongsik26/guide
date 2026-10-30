package com.guide.ex.dto.member;

import com.guide.ex.dto.post.PostDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private Long memberId;

    @NotEmpty
    @JsonIgnore
    private String uid;
    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;

    @JsonIgnore
    private String salt;


    @NotEmpty
    private String name;
    @NotEmpty
    @JsonIgnore
    private String phone;
    @NotEmpty
    private String gender;

    private Year year;
    private String rating;
    private boolean isBan;


    //마이페이지 왼쪽 하단 사용
    private int postCount;
    private int commentCount;
    private int likeCount;

    private void change(String name) {
        this.name = name;
    }

    private List<PostDTO> posts;

    private MemberProfileDTO profileInfo;

    public void setProfileInfo(MemberProfileDTO profileInfo) {
        this.profileInfo = profileInfo;
        // MemberProfileDTO에 MemberDTO의 memberId를 설정
        if (profileInfo != null && this.memberId != null) {
            profileInfo.setMemberId(this.memberId);
        }
    }

    public String getAgeRange() {
        if (this.year == null) return "";
        int currentYear = LocalDate.now().getYear(); // 현재 연도를 구함
        int birthYear = this.year.getValue(); // Year 객체에서 연도를 int로 추출
        int age = currentYear - birthYear; // 실제 나이 계산
        int rangeStart = (age / 10) * 10; // 연령대 계산
        return rangeStart + "대"; // "20대", "30대" 등의 문자열 반환
    }

    public String getGenderDescription() {
        if (gender == null) return "정보 없음"; // gender 정보가 없을 경우 처리
        return gender.equals("male") ? "남자" : "여자";
    }
}
