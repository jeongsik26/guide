package com.guide.ex.service;

import com.guide.ex.dto.chat.ChatMessageDTO;
import com.guide.ex.dto.chat.ChatRoomDTO;

import java.util.List;

public interface ChatService {
    Long createChatRoom(ChatRoomDTO chatRoomDTO);

    void sendMessage(ChatMessageDTO chatMessageDTO);

    List<ChatRoomDTO> memberChatRooms(long memberId);

    boolean isRoomMember(Long roomId, Long memberId);

    List<ChatMessageDTO> roomMessages(Long roomId);

//    void deleteChatRoom(Long roomId);
}