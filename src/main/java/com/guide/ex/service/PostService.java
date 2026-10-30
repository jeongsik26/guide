package com.guide.ex.service;

import com.guide.ex.domain.post.Post;
import com.guide.ex.dto.post.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.util.List;

public interface PostService {
    void carrotRegister(CarrotDTO carrotDTO, MultipartFile file, HttpSession session);
    void joinRegister(JoinDTO joinDTO, MultipartFile file, HttpSession session);
    void reviewRegister(ReviewDTO reviewDTO, MultipartFile file, HttpSession session);
    void carrotModify(PostDTO postDTO, CarrotDTO carrotDTO, ImageDTO imageDTO);
    void reviewModify(PostDTO postDTO, ReviewDTO reviewDTO, ImageDTO imageDTO);
    void joinModify(PostDTO postDTO, JoinDTO joinDTO, ImageDTO imageDTO);
    Post postDetailRead(Long memberId);
//    Page<PostDTO> postTypeReadAll(String postType,int size, int page);
    Page<PostDTO> postSelectAll(String searchValue, String postType, Pageable pageable);

    Page<CarrotDTO> carrotTypeReadAll(int size, int page, Sort sort);
    Page<ReviewDTO> reviewTypeReadAll(int size, int page, Sort sort);
    Page<JoinDTO> joinTypeReadAll(int size, int page, Sort sort);

    boolean deletePost(Long postId, Long memberId);
    String findPostTypeByPostId(Long postId);

    void updatePostCommentCount(Long postId);
}
