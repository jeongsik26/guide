package com.guide.ex.repository.post;


import com.guide.ex.domain.member.Member;
import com.guide.ex.domain.post.Comment;
import com.guide.ex.domain.post.Post;
import com.guide.ex.repository.search.CommentRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Log4j2
public class commentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void testInsert() {

        // 실제 DB에 있는 postId
        Long memberId = 2L;
        Long postId = 1L;

        Member member = Member.builder().memberId(memberId).build();
        Post post = Post.builder().postId(postId).build();

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .commentContent("댓글 insert 테스트")
                .build();
        commentRepository.save(comment);
    }

    @Transactional
    @Test
    public void testFindByPostId() {
        Long postId = 1L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("commentId"));

        Page<Comment> result = commentRepository.listOfPost(postId, pageable);

        result.getContent().forEach(comment -> {
            log.info(comment);
        });
    }

    @Transactional
    @Test
    public void testFindByMemberId() {
        Long memberId = 2L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("commentId"));

        Page<Comment> result = commentRepository.listOfMember(memberId, pageable);

        result.getContent().forEach(comment -> {
            log.info(comment);
        });
    }

//    @Transactional
//    @Test
//    public void testFindByPostIdAndMemberId() {
//        Long postId = 1L;
//
//        Long memberId = 2L;
//
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("commentId"));
//
//        Page<Comment> result = commentRepository.listOfPostMember(postId, memberId, pageable);
//
//        result.getContent().forEach(comment -> {
//            log.info(comment);
//        });
//    }
}
