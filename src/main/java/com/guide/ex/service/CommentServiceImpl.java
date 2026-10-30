package com.guide.ex.service;

import com.guide.ex.domain.member.Member;
import com.guide.ex.domain.member.MemberProfile;
import com.guide.ex.domain.post.Comment;
import com.guide.ex.domain.post.Post;
import com.guide.ex.dto.PageRequestDTO;
import com.guide.ex.dto.PageResponseDTO;
import com.guide.ex.dto.member.MemberDTO;
import com.guide.ex.dto.member.MemberProfileDTO;
import com.guide.ex.dto.post.CommentDTO;
import com.guide.ex.dto.post.PostDTO;
import com.guide.ex.repository.member.MemberRepository;
import com.guide.ex.repository.post.PostRepository;
import com.guide.ex.repository.search.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    // 댓글 등록 처리
    @Override
    public Long register(CommentDTO commentDTO) {
        Comment comment = modelMapper.map(commentDTO, Comment.class);

        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Member member = memberRepository.findById(commentDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        comment.setPost(post);
        comment.setMember(member);

        Long commentId = commentRepository.save(comment).getCommentId();

        return commentId;
    }

    // 댓글 조회 처리
    @Override
    public CommentDTO read(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Comment comment = commentOptional.orElseThrow(() -> new RuntimeException("Comment not found"));

        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        if (comment.getPost() != null) {
            commentDTO.setPostId(comment.getPost().getId());
        }
        if (comment.getMember() != null) {
            commentDTO.setMemberId(comment.getMember().getId());
        }

        return commentDTO;
    }

    // 댓글 수정 처리
    @Override
    public void modify(CommentDTO commentDTO) {
        Optional<Comment> commentOptional = commentRepository.findById(commentDTO.getCommentId());

        Comment comment = commentOptional.orElseThrow();

        comment.change(commentDTO.getCommentContent());

        commentRepository.save(comment);
    }

    // 댓글 삭제 처리
    @Override
    public void remove(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    // 게시물별 댓글 페이징 처리
    @Override
    public PageResponseDTO<CommentDTO> getListOfPost(Long postId, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize());

        Page<Comment> result = commentRepository.listOfPost(postId, pageable);

        List<CommentDTO> dtoList = result.getContent().stream().map(comment -> {
            CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
            if (comment.getPost() != null) {
                commentDTO.setPostId(comment.getPost().getId());
            }
            if (comment.getMember() != null) {
                commentDTO.setMemberId(comment.getMember().getId());
                // 댓글 응답(JSON)에는 작성자 표시용 정보만 담는다(비밀번호·연락처 노출 금지)
                commentDTO.setMember(MemberDTO.builder()
                        .memberId(comment.getMember().getMemberId())
                        .name(comment.getMember().getName())
                        .build());
            }
            return commentDTO;
        }).collect(Collectors.toList());

        return PageResponseDTO.<CommentDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    // 사용자별 댓글 페이징 처리
    @Override
    public PageResponseDTO<CommentDTO> getListOfMember(Long memberId, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize());

        Page<Comment> result = commentRepository.listOfMember(memberId, pageable);

        List<CommentDTO> dtoList = result.getContent().stream().map(comment -> {
            CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
            if (comment.getPost() != null) {
                commentDTO.setPostId(comment.getPost().getId());
            }
            if (comment.getMember() != null) {
                commentDTO.setMemberId(comment.getMember().getId());
            }
            return commentDTO;
        }).collect(Collectors.toList());

        return PageResponseDTO.<CommentDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

//    @Override
//    public PageResponseDTO<CommentDTO> getListOfPostMember(Long postId, Long memberId, PageRequestDTO pageRequestDTO) {
//        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <=0? 0: pageRequestDTO.getPage() -1,
//                pageRequestDTO.getSize(),
//                Sort.by("postId").ascending());
//
//        Page<Comment> result = commentRepository.listOfPostMember(postId, memberId, pageable);
//
//        List<CommentDTO> dtoList = result.getContent().stream().map(comment -> {
//            CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
//            if (comment.getPost() != null) {
//                commentDTO.setPostId(comment.getPost().getId());
//            }
//            if (comment.getMember() != null) {
//                commentDTO.setMemberId(comment.getMember().getId());
//            }
//            return commentDTO;
//        }).collect(Collectors.toList());
//
//        return PageResponseDTO.<CommentDTO>withAll()
//                .pageRequestDTO(pageRequestDTO)
//                .dtoList(dtoList)
//                .total((int)result.getTotalElements())
//                .build();
//    }

}
