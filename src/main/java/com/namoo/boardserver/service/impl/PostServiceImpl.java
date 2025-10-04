package com.namoo.boardserver.service.impl;

import com.namoo.boardserver.dto.PostDTO;
import com.namoo.boardserver.dto.UserDTO;
import com.namoo.boardserver.mapper.PostMapper;
import com.namoo.boardserver.mapper.UserProfileMapper;
import com.namoo.boardserver.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public void register(String id, PostDTO postDTO) {
        UserDTO memberInfo = userProfileMapper.getUserProfile(id);
        postDTO.setUserId(memberInfo.getId());
        postDTO.setCreateTime(new Date());

        if(memberInfo != null){
            postMapper.register(postDTO);
        } else{
            log.error("register Error! {}", postDTO);
            throw new RuntimeException("register ERROR! 게시글 등록 메서드를 확인하세요" + postDTO);
        }
    }

    @Override
    public List<PostDTO> getMyPosts(int accountId) {
        List<PostDTO> postDTOList = postMapper.selectMyProducts(accountId);
        return postDTOList;
    }

    @Override
    public void updatePosts(PostDTO postDTO) {
        if(postDTO != null && postDTO.getId() != 0){
            postMapper.updateProducts(postDTO);
        } else {
            log.error("update Error! {}", postDTO);
            throw new RuntimeException("update ERROR! 게시글 수정 메서드를 확인하세요" + postDTO);
        }
    }

    @Override
    public void deletePosts(int userId, int postId) {
        if(userId != 0 && postId != 0){
            postMapper.deleteProducts(postId);
        } else {
            log.error("delete Error! {}", postId);
            throw new RuntimeException("delete ERROR! 게시글 삭제 메서드를 확인하세요" + postId);
        }
    }
}
