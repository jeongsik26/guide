package com.guide.ex.controller.post;

import com.guide.ex.dto.PageRequestDTO;
import com.guide.ex.dto.PageResponseDTO;
import com.guide.ex.dto.post.CommentDTO;
import com.guide.ex.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/comment")
@Log4j2
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Comment Post", description = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> register(
                @Valid @RequestBody CommentDTO commentDTO,
                BindingResult bindingResult, HttpSession session)throws BindException {
        // 작성자는 요청 본문이 아닌 로그인 세션 기준
        commentDTO.setMemberId(loginMemberId(session));
        log.info("register: {}", commentDTO);

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("commentId", commentDTO.getCommentId());
        resultMap.put("postId", commentDTO.getPostId());
        resultMap.put("memberId", commentDTO.getMemberId());

        Long commentId = commentService.register(commentDTO);

        resultMap.put("commentId", commentId);

        return resultMap;
    }

    @Operation(summary = "Comments of Post", description = "GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value = "/post/{postId}")
    public PageResponseDTO<CommentDTO> getList1(@PathVariable("postId") Long postId,
                                             PageRequestDTO pageRequestDTO){

        PageResponseDTO<CommentDTO> responseDTO = commentService.getListOfPost(postId, pageRequestDTO);

        return responseDTO;
    }

    @Operation(summary = "Comments of Member", description = "GET 방식으로 특정 사용자의 댓글 목록")
    @GetMapping(value = "/member/{memberId}")
    public PageResponseDTO<CommentDTO> getList2(@PathVariable("memberId") Long memberId,
                                               PageRequestDTO pageRequestDTO){

        PageResponseDTO<CommentDTO> responseDTO = commentService.getListOfMember(memberId, pageRequestDTO);

        return responseDTO;
    }

    @Operation(summary = "Read Comment", description = "GET 방식으로 특정 댓글 조회")
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentDTO(@PathVariable("commentId") Long commentId) {
        try {
            CommentDTO commentDTO = commentService.read(commentId);
            return ResponseEntity.ok(commentDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Modify Comment", description = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE )
    public Map<String,Long> modify( @PathVariable("commentId") Long commentId, @RequestBody CommentDTO commentDTO,
                                    HttpSession session ){

        checkOwner(commentId, session);
        commentDTO.setCommentId(commentId); //번호를 일치시킴

        commentService.modify(commentDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("commentId", commentId);

        return resultMap;
    }

    @Operation(summary = "Delete Comment", description = "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{commentId}")
    public Map<String,Long> remove( @PathVariable("commentId") Long commentId, HttpSession session ){

        checkOwner(commentId, session);
        commentService.remove(commentId);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("commentId", commentId);

        return resultMap;
    }

    private Long loginMemberId(HttpSession session) {
        Long memberId = (Long) session.getAttribute("member_id");
        if (memberId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return memberId;
    }

    // 본인이 작성한 댓글만 수정/삭제 가능
    private void checkOwner(Long commentId, HttpSession session) {
        Long memberId = loginMemberId(session);
        if (!memberId.equals(commentService.read(commentId).getMemberId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 댓글만 수정/삭제할 수 있습니다.");
        }
    }

}
