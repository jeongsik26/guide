package com.guide.ex.domain.chat;

import java.time.LocalDateTime;
import com.guide.ex.domain.member.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @NotNull
    @Column(name = "chat_msg", nullable = false)
    private String chatMsg;

    //@CreatedDate
    //@Column(name = "regist_date", nullable = false, updatable = false)
    //private LocalDateTime registDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom roomId;

    public void setChatRoom(ChatRoom roomId) {
        this.roomId = roomId;
    }
}
