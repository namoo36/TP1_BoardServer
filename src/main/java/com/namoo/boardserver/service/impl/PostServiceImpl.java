package com.namoo.boardserver.service.impl;

import com.namoo.boardserver.dto.CommentDTO;
import com.namoo.boardserver.dto.PostDTO;
import com.namoo.boardserver.dto.TagDTO;
import com.namoo.boardserver.dto.UserDTO;
import com.namoo.boardserver.mapper.CommentMapper;
import com.namoo.boardserver.mapper.PostMapper;
import com.namoo.boardserver.mapper.TagMapper;
import com.namoo.boardserver.mapper.UserProfileMapper;
import com.namoo.boardserver.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TagMapper tagMapper;

    @CacheEvict(value="getProducts", allEntries = true)
    @Override
    public void register(String id, PostDTO postDTO) {
        UserDTO memberInfo = userProfileMapper.getUserProfile(id);
        postDTO.setUserId(memberInfo.getId());
        postDTO.setCreateTime(new Date());

        if(memberInfo != null){
            postMapper.register(postDTO);
            Integer postId = postDTO.getId();
            for(TagDTO tagDTO : postDTO.getTagDTOList()){
                tagMapper.register(tagDTO);
                Integer tagId = tagDTO.getId();
                tagMapper.createPostTag(tagId, postId);
            }

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

    @Override
    public void registerComment(CommentDTO commentDTO) {
        if(commentDTO.getPostId() != 0){
            commentMapper.register(commentDTO);
        } else {
            log.error("registerComment Error! {}", commentDTO);
            throw new RuntimeException("register Comment! " + commentDTO);
        }
    }

    @Override
    public void updateComment(CommentDTO commentDTO) {
        if(commentDTO != null){
            commentMapper.register(commentDTO);
        } else {
            log.error("updateComment Error!");
            throw new RuntimeException("update Comment! " + commentDTO);
        }
    }

    @Override
    public void deleteComment(int userId, int commentId) {
        if(userId != 0 && commentId != 0){
            commentMapper.deletePostComment(commentId);
        } else {
            log.error("deleteComment Error!");
            throw new RuntimeException("deleteComment " + commentId);
        }
    }

    @Override
    public void registerTag(TagDTO tagDTO) {
        if(tagDTO != null){
            tagMapper.register(tagDTO);
        } else {
            log.error("registerTag Error! {}", tagDTO);
            throw new RuntimeException("registerTag" + tagDTO);
        }
    }

    @Override
    public void updateTag(TagDTO tagDTO) {
        if(tagDTO != null){
            tagMapper.updateTags(tagDTO);
        } else {
            log.error("updateTag Error!");
            throw new RuntimeException("updateTag");
        }
    }

    @Override
    public void deletePostTag(int userId, int tagId) {
        if(userId != 0 && tagId != 0){
            tagMapper.deletePostTag(tagId);
        } else {
            log.error("deleteTag Error!");
            throw new RuntimeException("deleteTag Error!");
        }
    }
}
