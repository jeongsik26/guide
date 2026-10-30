package com.guide.ex.service;

import com.guide.ex.domain.chat.ChatMessage;
import com.guide.ex.domain.chat.ChatRoom;
import com.guide.ex.domain.member.Member;
import com.guide.ex.domain.member.MemberProfile;
import com.guide.ex.dto.chat.ChatMessageDTO;
import com.guide.ex.dto.chat.ChatRoomDTO;
import com.guide.ex.dto.member.MemberDTO;
import com.guide.ex.dto.member.MemberProfileDTO;
import com.guide.ex.repository.chat.ChatMessageRepository;
import com.guide.ex.repository.chat.ChatRoomRepository;
import com.guide.ex.repository.member.MemberProfileRepository;
import com.guide.ex.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long createChatRoom(ChatRoomDTO chatRoomDTO) {

        Member sender = memberRepository.findById(chatRoomDTO.getSenderId()).orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        Member receiver = memberRepository.findById(chatRoomDTO.getReceiverId()).orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        // 존재하는 채팅방 확인
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findRoomBySenderAndReceiver(sender, receiver);
        if (existingChatRoom.isPresent()) {
            return existingChatRoom.get().getRoomId(); // Return the existing chat room ID
        } else {

            // Create a new chat room
            ChatRoom chatRoom = ChatRoom.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .chatMessages(new ArrayList<>())
                    .build();

            // Save the chat room
            chatRoom = chatRoomRepository.save(chatRoom);

            // Return the ID of the newly created chat room
            return chatRoom.getRoomId();
        }
    }


    @Override
    public void sendMessage(ChatMessageDTO chatMessageDTO) {

        Member member = memberRepository.findById(chatMessageDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatMsg(chatMessageDTO.getChatMsg())
                .memberId(member)
                .roomId(chatRoom)
                .build();

        chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatRoomDTO> memberChatRooms(long memberId) {
        Optional<Member> result = memberRepository.findById(memberId);
        Member member = result.orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다.")); // 조회된 Member가 없을 경우 예외 발생

        List<ChatRoom> rooms = chatRoomRepository.findBySenderOrReceiver(member);

        List<ChatRoomDTO> memberChatrooms = rooms.stream()
                .map(room -> {
                    ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
                    chatRoomDTO.setRoomId(room.getRoomId());
                    chatRoomDTO.setSenderId(room.getSender().getId());
                    chatRoomDTO.setReceiverId(room.getReceiver().getId());

                    // Retrieve sender's profile
                    Optional<MemberProfile> senderProfileResult = memberProfileRepository.findByMember(room.getSender());
                    MemberProfile senderProfile = senderProfileResult.orElse(null); // 조회된 MemberProfile이 없을 경우 null을 반환

                    // Map sender information
                    chatRoomDTO.setSender(modelMapper.map(room.getSender(), MemberDTO.class));

                    // Set sender's image if available
                    if (senderProfile != null) {
                        chatRoomDTO.getSender().setProfileInfo(modelMapper.map(senderProfile, MemberProfileDTO.class));
                    } else {
                        chatRoomDTO.getSender().setProfileInfo(null);
                    }

                    // Retrieve receiver's profile
                    Optional<MemberProfile> receiverProfileResult = memberProfileRepository.findByMember(room.getReceiver());
                    MemberProfile receiverProfile = receiverProfileResult.orElse(null); // 조회된 MemberProfile이 없을 경우 null을 반환

                    // Map receiver information
                    chatRoomDTO.setReceiver(modelMapper.map(room.getReceiver(), MemberDTO.class));

                    // Set receiver's image if available
                    if (receiverProfile != null) {
                        chatRoomDTO.getReceiver().setProfileInfo(modelMapper.map(receiverProfile, MemberProfileDTO.class));
                    } else {
                        chatRoomDTO.getReceiver().setProfileInfo(null);
                    }

                    return chatRoomDTO;
                })
                .collect(Collectors.toList());

        return memberChatrooms;
    }

    // 채팅방 참여자(보낸 사람/받는 사람)인지 확인
    @Override
    public boolean isRoomMember(Long roomId, Long memberId) {
        return chatRoomRepository.findById(roomId)
                .map(room -> memberId.equals(room.getSender().getMemberId())
                        || memberId.equals(room.getReceiver().getMemberId()))
                .orElse(false);
    }

    // 채팅방의 이전 메시지(오래된 순)
    @Override
    public List<ChatMessageDTO> roomMessages(Long roomId) {
        return chatMessageRepository.findByRoomId_RoomIdOrderByMessageIdAsc(roomId).stream()
                .map(message -> ChatMessageDTO.builder()
                        .chatMsg(message.getChatMsg())
                        .memberId(message.getMemberId().getMemberId())
                        .memberName(message.getMemberId().getName())
                        .roomId(roomId)
                        .build())
                .collect(Collectors.toList());
    }
}
