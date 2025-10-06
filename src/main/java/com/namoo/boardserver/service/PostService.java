package com.namoo.boardserver.service;


import com.namoo.boardserver.dto.CommentDTO;
import com.namoo.boardserver.dto.PostDTO;
import com.namoo.boardserver.dto.TagDTO;

import java.util.List;

public interface PostService {
    // 게시글 등록
    void register(String id, PostDTO postDTO);

    // 게시글 조회
    List<PostDTO> getMyPosts(int accountId);


    // 업데이트
    void updatePosts(PostDTO postDTO);

    // 삭제
    void deletePosts(int userId, int postId);

    // 댓글
    void registerComment(CommentDTO commentDTO);

    void updateComment(CommentDTO commentDTO);

    void deleteComment(int userId, int commentId);

    // 태그 등록
    void registerTag(TagDTO tagDTO);

    // 태그 수정
    void updateTag(TagDTO tagDTO);

    void deletePostTag(int userId, int tagId);

}
