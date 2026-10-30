package com.guide.ex.domain.member;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "member")
public class MemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_profile_id")
    private Long memberProfileId;

    @Column(name="uuid", length = 500)
    private String uuid;

    @Column(name = "file_name", length = 200)
    private String fileName;

    @Column(length = 500)
    private String content;

    @Column(length = 20, nullable = false, name = "travel_type")
    private String travelType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // change()
    public void change(String uuid, String fileName, String content, String travelType) {
        this.uuid = uuid;
        this.fileName = fileName;
        this.content = content;
        this.travelType = travelType;
    }

    // Member 객체 설정 메소드 추가
    public void setMember(Member member) {
        this.member = member;
    }
}
