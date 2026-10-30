package com.guide.ex.repository.search;

import com.guide.ex.domain.member.QMember;
import com.guide.ex.domain.post.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("allPostSearchImpl")
@Log4j2
public class AllPostSearchImpl implements AllPostSearch {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public AllPostSearchImpl(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
        this.entityManager = entityManager;
        this.queryFactory = jpaQueryFactory;
    }

    // 거래 게시판 페이징 처리
    @Override
    public Page<Carrot> searchCarrotPaging(int size, int page, Sort sort) {
        return searchTypePaging(QCarrot.carrot._super, Carrot.class, "Carrot", size, page, sort);
    }

    // 후기 게시판 페이징 처리
    @Override
    public Page<Review> searchReviewPaging(int size, int page, Sort sort) {
        return searchTypePaging(QReview.review._super, Review.class, "Review", size, page, sort);
    }

    // 동행 모집 게시판 페이징 처리
    @Override
    public Page<Join> searchJoinPaging(int size, int page, Sort sort) {
        return searchTypePaging(QJoin.join._super, Join.class, "Join", size, page, sort);
    }

    // 게시판 유형별 페이징 공통 처리
    // 이미지(컬렉션)는 fetch join 하지 않는다: 페이징이 메모리에서 일어나고, 이미지가 없는 글이 빠지기 때문
    private <T extends Post> Page<T> searchTypePaging(QPost post, Class<T> type, String postType,
                                                      int size, int page, Sort sort) {
        Pageable pageable = PageRequest.of(Math.max(page, 1) - 1, size, sort);
        QMember member = QMember.member;

        BooleanBuilder where = new BooleanBuilder()
                .and(post.isDeleted.isFalse())
                .and(post.postType.eq(postType));

        List<T> content = queryFactory.selectFrom(post)
                .join(post.member, member).fetchJoin()
                .where(where)
                .orderBy(orderOf(post, sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(type::cast)
                .toList();

        Long total = queryFactory.select(post.count())
                .from(post)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    // 정렬 기준: views(조회순), comments/commentCount(댓글순), 그 외는 최신순
    private OrderSpecifier<?> orderOf(QPost post, Sort sort) {
        Sort.Order order = sort.stream().findFirst().orElse(null);
        String property = order == null ? "registerDate" : order.getProperty();

        return switch (property) {
            case "views" -> post.views.desc();
            case "comments", "commentCount" -> post.commentCount.desc();
            default -> post.registerDate.desc();
        };
    }

    // 게시글 제목, 내용, 닉네임이 포함된 게시글 검색
    @Override
    public Page<Post> searchPostContaining(String searchValue, String postType, Pageable pageable) {
        QPost post = QPost.post;
        QMember member = QMember.member;

        // 게시판 유형에 맞는 게시글만 필터링
        BooleanBuilder where = new BooleanBuilder()
                .and(post.isDeleted.isFalse())
                .and(post.postType.eq(postType));
        if (searchValue != null && !searchValue.trim().isEmpty()) {
            where.andAnyOf(
                    post.title.contains(searchValue),
                    post.content.contains(searchValue),
                    member.name.contains(searchValue)
            );
        }

        List<Post> results = queryFactory.selectFrom(post)
                .join(post.member, member).fetchJoin()
                .where(where)
                .orderBy(orderOf(post, pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(post.count())
                .from(post)
                .join(post.member, member)
                .where(where)
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }

    // 게시글 상세 보기
    @Transactional
    @Override
    public Post searchOne(Long postId) {
        updateViews(postId);
        // 벌크 update 후 영속성 컨텍스트에 남은 이전 조회수를 버린다
        entityManager.clear();
        return findPostById(postId);
    }

    private Post findPostById(Long postId) {
        QPost qPost = QPost.post;

        Post result = queryFactory.selectFrom(qPost)
                .where(qPost.postId.eq(postId).and(qPost.isDeleted.isFalse()))
                .fetchOne();
        if (result == null) {
            throw new EntityNotFoundException("게시글을 찾을 수 없습니다. ID: " + postId);
        }
        return result;
    }

    @Transactional
    @Override
    public void updateViews(Long postId) {
        // 조회수 증가 로직
        QPost post = QPost.post;
        queryFactory.update(post)
                .set(post.views, post.views.add(1))
                .where(post.postId.eq(postId))
                .execute();
    }

    @Transactional
    @Override
    public boolean deleteOne(Long postId, Long memberId) {
        // postId로 게시글을 조회하고 memberId로 소유권을 확인
        QPost qpost = QPost.post;
        Post post = queryFactory.selectFrom(qpost)
                .where(qpost.postId.eq(postId)
                        .and(qpost.member.memberId.eq(memberId))) // 게시글의 memberId가 입력 memberId와 일치하는지 확인
                .fetchOne();

        // 게시글이 존재하고 사용자가 삭제 권한을 가지고 있는지 확인
        if (post != null) {
            entityManager.remove(post);
            entityManager.flush();
            log.info("게시글 삭제 완료!! : {}", postId);
            return true;  // 성공적으로 삭제됨
        }
        return false;  // 삭제 실패 (게시글이 존재하지 않거나 권한 없음)
    }
}
