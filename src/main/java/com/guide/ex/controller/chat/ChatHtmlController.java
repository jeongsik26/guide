package com.guide.ex.controller.chat;

import com.guide.ex.dto.chat.ChatRoomDTO;
import com.guide.ex.service.ChatService;
import com.guide.ex.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/chat")
@Controller
public class ChatHtmlController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ChatService chatService;


//    @MessageMapping("/chat/message")
//    public void sendMessage(ChatMessage message) {
//        ChatRoom room = chatRoomRepository.findById(message.getChatRoom().getRoomId()).orElse(null);
//        if (room != null) {
//            chatMessageRepository.save(message);
//            messagingTemplate.convertAndSend("/sub/chat/room/" + room.getRoomId(), message);
//        }
//    }

    @GetMapping("/chatList")
    public String getRoomById(HttpSession session, Model model) {
        Long memberId = (Long) session.getAttribute("member_id");

        if (memberId == null) {
            return "redirect:/member/login";
        }

        List<ChatRoomDTO> rooms = chatService.memberChatRooms(memberId);
        model.addAttribute("rooms", rooms);
        return "chat/chatList";

    }

    @GetMapping("/chatRoom/{roomId}")
    public String chatRoom(@PathVariable("roomId") Long roomId, HttpSession session, Model model) {
        Long memberId = (Long) session.getAttribute("member_id");
        if (memberId == null) {
            return "redirect:/member/login";
        }
        if (!chatService.isRoomMember(roomId, memberId)) {
            return "redirect:/chat/chatList";
        }
        String name = memberService.memberReadOne(memberId).getName();

        model.addAttribute("member_name", name);
        model.addAttribute("session_member_id", memberId);
        model.addAttribute("room_id", roomId); // 채팅방 ID를 모델에 추가

        return "chat/chatRoom"; // chatRoom.html 페이지 반환
    }
}