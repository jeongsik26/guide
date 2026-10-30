package com.guide.ex.repository.chat;

import com.guide.ex.domain.chat.ChatBlocked;
import com.guide.ex.repository.chat.ChatBlockedRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class ChatBlockedRepositoryTests {

    @Autowired
    private ChatBlockedRepository chatBlockedRepository;

//    @Test
//    public void testTime(){
//        log.info("------------test time-------------");
//        chatBlockedRepository.getTime();
//    }

    @Test
    public void testInsertChatBlocked() {

        LocalDateTime now = LocalDateTime.now();

        ChatBlocked chatBlocked = ChatBlocked.builder()
                .blockedUser("hoje")
                .registDate(now)
                .build();

        chatBlockedRepository.save(chatBlocked);
    }

    @Test
    public void testSelectChatMessage(){
        Long blockedId = 1L;

        Optional<ChatBlocked> result = chatBlockedRepository.findById(blockedId);
    }

    @Test
    public void testUpdate(){
        Long blockedId = chatBlockedRepository.save(ChatBlocked.builder()
                .blockedUser("update")
                .registDate(LocalDateTime.now())
                .build()).getBlockedID();

        Optional<ChatBlocked> result = chatBlockedRepository.findById(blockedId);

        ChatBlocked chatBlocked = result.orElseThrow();

        chatBlocked.change("update...22-----", "update room_id 25------");

        /* save: pk가 테이블에 존재하지 않으면 => 삽입 => insert문
                 pk 기존 테이블에 존재하면 => 수정 => update문
        * */
        chatBlockedRepository.save(chatBlocked);
    }

    @Test
    public void testDelete(){
        Long blockcedId = 1L;

        chatBlockedRepository.deleteById(blockcedId);
    }
}
