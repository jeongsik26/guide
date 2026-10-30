package com.guide.ex.service;

import com.guide.ex.domain.member.Member;
import com.guide.ex.domain.member.MemberProfile;
import com.guide.ex.dto.chat.ChatRoomDTO;
import com.guide.ex.dto.member.MemberDTO;
import com.guide.ex.dto.member.MemberProfileDTO;
import com.guide.ex.repository.member.MemberProfileRepository;
import com.guide.ex.repository.member.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import java.time.Year;
import java.util.List;

@SpringBootTest
@Log4j2
public class ChatServiceTests {

    @Autowired
    private ChatService chatService;

    @Test
    @Transactional
    public void findByMember() {
        List<ChatRoomDTO> chatRoomDTOList = chatService.memberChatRooms(5l);
        log.info("==================================================================");
        log.info(chatRoomDTOList.toString());
        log.info("==================================================================");
    }


}
