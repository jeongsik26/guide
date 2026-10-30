package com.guide.ex.controller.chat;

import com.guide.ex.dto.chat.ChatMessageDTO;
import com.guide.ex.service.ChatService;
import com.guide.ex.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;
    private final MemberService memberService;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/message")
    public void sendMessage(ChatMessageDTO chatMessageDTO, SimpMessageHeaderAccessor headerAccessor) {
        // 보낸 사람은 클라이언트 값이 아닌 웹소켓 연결 시 복사한 로그인 세션 기준
        Map<String, Object> attrs = headerAccessor.getSessionAttributes();
        Long memberId = attrs == null ? null : (Long) attrs.get("member_id");
        Long roomId = chatMessageDTO.getRoomId();
        if (memberId == null || roomId == null || !chatService.isRoomMember(roomId, memberId)) {
            return;
        }
        if (chatMessageDTO.getChatMsg() == null || chatMessageDTO.getChatMsg().isBlank()) {
            return;
        }

        chatMessageDTO.setMemberId(memberId);
        chatMessageDTO.setMemberName(memberService.memberReadOne(memberId).getName());
        chatService.sendMessage(chatMessageDTO);
        messagingTemplate.convertAndSend("/topic/chat/room/" + roomId, chatMessageDTO);
    }
}
