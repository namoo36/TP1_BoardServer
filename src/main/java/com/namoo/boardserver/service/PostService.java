package com.namoo.boardserver.service;


import com.namoo.boardserver.dto.PostDTO;

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

}
