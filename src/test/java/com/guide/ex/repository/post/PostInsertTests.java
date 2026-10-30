package com.guide.ex.repository.post;

import com.guide.ex.domain.member.Member;
import com.guide.ex.domain.post.*;
import com.guide.ex.repository.member.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest
@Log4j2
public class PostInsertTests {

//    @Autowired
//    private CommentRepository testComment;

    @Autowired
    private LikeRepository testLike;

    @Autowired
    private JoinRepository postJoin;

    @Autowired
    private CarrotRepository testCarrot;

    @Autowired
    private ReviewRepository testReview;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;



//    @Test
//    public void carrotSearch1() {
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("post_id").descending());
//        testCarrot.findAll(pageable).forEach(System.out::println);
//    }


//    @Test
//    public void carrotSearchWithQuerydsl() {
//        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
//        QCarrot qCarrot = new QCarrot("carrot");
//
//        // Paging 및 정렬 설정
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("postId").descending());
//
//        // Querydsl을 사용하여 검색 쿼리 생성
//        Page<Carrot> result = testCarrot.findAll(qCarrot.post.title.contains("Title"), pageable);
//
//        // 검색 결과 출력
//        result.getContent().forEach(System.out::println);
//    }


    @Test
    public void testInsert() {
        log.info("------------test testInsert-------------");

        Carrot carrot = Carrot.builder()
                .title("Title입니다.")
                .content("내용입니다아아아아아앙")
                .registerDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now().plusDays(5))
                .locationX(BigDecimal.valueOf(23.1234567))
                .locationY(BigDecimal.valueOf(33.2268567))
                .postType("Carrot")
                .price(152500)
                .build();

        Carrot carrot1 = testCarrot.save(carrot);

        assertNotNull(carrot1);
    }

    @RepeatedTest(5)
    public void testFindById() {
        testInsert();
    }

    @Test
    public void testSelect() {
        Member member = memberRepository.save(Member.builder()
                .name("tester")
                .uid("tester" + System.nanoTime())
                .pwd("pwd")
                .salt("")
                .phone("01012345678")
                .year(java.time.Year.of(1999))
                .gender("male")
                .build());
        Long postId = testCarrot.save(Carrot.builder()
                .title("select")
                .content("select")
                .price(1000)
                .postType("Carrot")
                .member(member)
                .build()).getPostId();

        Optional<Carrot> result = testCarrot.findById(postId);

        Carrot carrot = result.get();
        log.info(carrot);
    }

    @Test
    public void testUpdate() {
        Long postId = 5L;
        Optional<Carrot> optional = testCarrot.findById(postId);
        if (optional.isPresent()) {
            Carrot carrot = optional.get();
            log.info("Found post: {}", carrot);
//            carrot.change("update..title입니다", "내용수정함");
            carrot.getRegisterDate();
            carrot = testCarrot.save(carrot);
        }
    }

    @Test
    public void testDelete() {
        Long postId = 6L;
        Optional<Carrot> optional = testCarrot.findById(postId);
        if (optional.isPresent()) {
            Carrot carrot = optional.get();
            log.info("Found post: {}", carrot);
            testCarrot.delete(carrot);
        }
    }

    //---------------------------Comment-------------------
//    @Test
//    public void testInsertComment() {
//        Comment comment = Comment.builder()
//                .content("ㅋㅋㅋㅋ집가고싶다.")
//                .registerDate(LocalDateTime.now())
//                .modifyDate(LocalDateTime.now())
//                .build();
//        Comment com = testComment.save(comment);
//
//        log.info(com);
//    }

//    @Test
//    public void testCommentUpdate() {
//
//        Long bno = 1L;
//
//        Optional<Comment> result = testComment.findById(bno);
//
//        Comment comment = result.orElseThrow();
//
//        comment.change("update..title 100");
//
//        testComment.save(comment);
//
//    }
//
//    @Test
//    public void testCommentDelete() {
//        Long bno = 2L;
//
//        testComment.deleteById(bno);
//    }


    //----------------like-------------------
    @Test
    public void testSaveLike() {
        // 좋아요 엔티티 생성
        Like like = Like.builder()
                .registerDate(LocalDateTime.now())
                // 다른 필요한 필드 설정
                .build();

        // 좋아요 저장
        Like savedLike = testLike.save(like);
        log.info("Saved Like: {}", savedLike);
    }

    //-----------------------join
    @Test
    public void testJoinInsert() {
        log.info("------------test testInsert-------------");

        Join join = Join.builder()
                .title("Title입니다.")
                .content("내용입니다아아아아아앙")
                .registerDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now().plusDays(5))
                .locationX(BigDecimal.valueOf(23.1234567))
                .locationY(BigDecimal.valueOf(33.2268567))
                .postType("Join")
                .expense(530000)
                .numPeople(3)
                .startTravelDate(LocalDate.now())
                .endTravelDate(LocalDate.now().plusDays(5))
                .build();

        Join join1 = postJoin.save(join);

        log.info(join1.getPostId());

        assertNotNull(join1);
    }

    @RepeatedTest(5)
    public void testInsertMany() {
        testJoinInsert();
    }

    @Test
    public void testJoinDelete() {
        Long bno = 12L;

        postJoin.deleteById(bno);
    }

    @Test
    public void testReviewInsert() {
        log.info("------------test testInsert-------------");

        Review review = Review.builder()
                .title("어린이 공원 타조농장 후기 남긴다.")
                .content("리뷰 ) 여기 재미 없다. 가지마라.")
                .registerDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now().plusDays(5))
                .locationX(BigDecimal.valueOf(23.1234567))
                .locationY(BigDecimal.valueOf(33.2268567))
                .postType("Review")
                .expense(70000)
                .grade(140000L)
                .startTravelDate(LocalDate.now())
                .endTravelDate(LocalDate.now().plusDays(2))
                .build();

        Review review1 = testReview.save(review);

        log.info(review1.getPostId());

        assertNotNull(review1);
    }

    @RepeatedTest(5)
    public void testReviewMany() {
        testReviewInsert();
    }

    @Test
    public void testReviewDelete() {
        Long bno = 18L;

        testReview.deleteById(bno);
    }

//    @Test
//    public void testPost() {
//        Member member = Member.builder().memberId(13L).build();
//        Post post = Post.builder()
//                .member(member)
//                .price(19000)
//                .build();
//
//    }

    @Test
    @Transactional
    public void testSelectCarrot() {
        List<Carrot> carrotList = testCarrot.findAllCarrotsWithPostDetails();
        log.info(carrotList);
    }

    @Test
    @Transactional
    public void memberPostTests() {
        Optional<Member> result = memberRepository.findById(3L);
        Member member = result.orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다.")); // 조회된 Member가 없을 경우 예외 발생

        Optional<List<Post>> memberPostsResult = postRepository.findAllByMember(member);
        List<Post> memberPosts = null;

        if (memberPostsResult.isPresent()) {
            memberPosts = memberPostsResult.get();
        }

        for (Post post : memberPosts) {
            System.out.println("게시물 번호: " + post.getPostId());
            System.out.println("게시물 제목: " + post.getTitle());
            System.out.println("게시물 타입: " + post.getPostType());
            System.out.println("===================================");
        }

    }

    @Test
    @Transactional
    public void memberPostPagingTests() {
        Optional<Member> result = memberRepository.findById(3L);
        Member member = result.orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다.")); // 조회된 Member가 없을 경우 예외 발생

        Pageable pageable = PageRequest.of(0, 5); // Get the first page with 10 items
        Page<Post> postsPage = postRepository.findAllByMember(member, pageable);

        List<Post> memberPosts = postsPage.getContent();
        for (Post post : memberPosts) {
            System.out.println("게시물 번호: " + post.getPostId());
            System.out.println("게시물 제목: " + post.getTitle());
            System.out.println("게시물 타입: " + post.getPostType());
            System.out.println("===================================");
        }
    }
}