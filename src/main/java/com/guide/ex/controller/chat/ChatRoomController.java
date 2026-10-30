package com.guide.ex.controller.chat;

import com.guide.ex.dto.chat.ChatRoomDTO;
import com.guide.ex.service.ChatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ChatRoomController {

    private final ChatService chatService;

    // 생성자 주입
    public ChatRoomController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/api/chat/create")
    public ResponseEntity<?> createChatRoom(@RequestParam("receiverId") Long receiverId,
                                            HttpSession session) {
        // 보내는 사람은 로그인 세션 기준(요청의 senderId 는 사용하지 않음)
        Long senderId = (Long) session.getAttribute("member_id");
        if (senderId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
        }
        if (senderId.equals(receiverId)) {
            return ResponseEntity.badRequest().body(Map.of("message", "자기 자신과는 채팅할 수 없습니다."));
        }
        ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        Long roomId = chatService.createChatRoom(chatRoomDTO);
        return ResponseEntity.ok().body(Map.of("roomId", roomId));
    }

    // 채팅방 이전 메시지 조회(참여자만)
    @GetMapping("/api/chat/room/{roomId}/messages")
    public ResponseEntity<?> roomMessages(@PathVariable Long roomId, HttpSession session) {
        Long memberId = (Long) session.getAttribute("member_id");
        if (memberId == null || !chatService.isRoomMember(roomId, memberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(chatService.roomMessages(roomId));
    }
}