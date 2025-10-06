package com.namoo.boardserver.controller;

import com.namoo.boardserver.aop.LoginCheck;
import com.namoo.boardserver.dto.*;
import com.namoo.boardserver.dto.response.CommonResponse;
import com.namoo.boardserver.service.impl.PostServiceImpl;
import com.namoo.boardserver.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/posts")
@Log4j2
@RequiredArgsConstructor
public class PostController {

    private final UserServiceImpl userService;
    private final PostServiceImpl postService;

    // 게시글 등록
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)   // 관리자만 카테고리 수정이 가능함
    public ResponseEntity<CommonResponse<PostDTO>> registerPost(String accountId,
                                                                @RequestBody PostDTO postDTO){
        postService.register(accountId, postDTO);
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "Register Post", postDTO);
//        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping
    @LoginCheck(type = LoginCheck.UserType.USER)   // 관리자만 카테고리 수정이 가능함
    public ResponseEntity<CommonResponse<PostDTO>> myPostInfo(String accountId){
        UserDTO memberInfo = userService.getUserInfo(accountId);
        List<PostDTO> postDTOList = postService.getMyPosts(memberInfo.getId());
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "My Posts Info ", postDTOList);
        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("{postId}")
    @LoginCheck(type = LoginCheck.UserType.USER)   // 관리자만 카테고리 수정이 가능함
    public ResponseEntity<CommonResponse<PostResponse>> updatePosts(String accountId,
                                                    @PathVariable(name="postId") int postId,
                                                    @RequestBody PostRequest postRequest){
        UserDTO memberInfo = userService.getUserInfo(accountId);
        PostDTO postDTO = PostDTO.builder()
                .id(postId)
                .name(postRequest.getName())
                .contents(postRequest.getContents())
                .views(postRequest.getViews())
                .categoryId(postRequest.getCategoryId())
                .userId(memberInfo.getId())
                .fileId(postRequest.getFileId())
                .updateTime(new Date())
                .build();

        postService.updatePosts(postDTO);
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "Update Posts", postDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("{postId}")
    @LoginCheck(type = LoginCheck.UserType.USER)   // 관리자만 카테고리 수정이 가능함
    public ResponseEntity<CommonResponse<PostDTO>> deletePost(String accountId,
                                                              @PathVariable(name = "postId") int postId,
                                                              @RequestBody PostDeleteRequest postDeleteRequest){
        UserDTO memberInfo = userService.getUserInfo(accountId);
        postService.deletePosts(postId, postDeleteRequest.getId());
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "delete Post ", postDeleteRequest);
        return ResponseEntity.ok(commonResponse);
    }

    // -- comments --

    @PostMapping("comments")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> registerPostComment(String accountId, @RequestBody CommentDTO commentDTO){
        postService.registerComment(commentDTO);
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "registerPostComment", commentDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("comments/{commentId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> updatePostComment(String accountId,
                                                                        @PathVariable(name = "commentId") int commentId,
                                                                        @RequestBody CommentDTO commentDTO){
        UserDTO memberInfo = userService.getUserInfo(accountId);
        if(memberInfo != null){
            postService.updateComment(commentDTO);
        }
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "updatePostComment", commentDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("comments/{commentId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> deletePostComment(String accountId,
                                                                        @PathVariable(name = "commentId") int commentId,
                                                                        @RequestBody CommentDTO commentDTO){
        UserDTO memberInfo = userService.getUserInfo(accountId);
        if(memberInfo != null){
            postService.deleteComment(memberInfo.getId(), commentId);
        }
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "deletePostComment", commentDTO);
        return ResponseEntity.ok(commonResponse);
    }


    //  tag
    @PostMapping("tags")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<TagDTO>> registerPostTag(String accountId, @RequestBody TagDTO tagDTO){
        postService.registerTag(tagDTO);
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "registerPostTag", tagDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @PatchMapping("tags/{tagId}")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<TagDTO>> updatePostTag(String accountId,
                                                                @PathVariable(name = "tagId") int tagId,
                                                                @RequestBody TagDTO tagDTO){
        UserDTO memberInfo = userService.getUserInfo(accountId);
        if(memberInfo != null){
            postService.updateTag(tagDTO);
        }
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "updatePostTag", tagDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("tags/{tagId}")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<TagDTO>> deletePostTag(String accountId,
                                                                @PathVariable(name = "tagId") int tagId,
                                                                @RequestBody TagDTO tagDTO){
        UserDTO memberInfo = userService.getUserInfo(accountId);
        if(memberInfo != null){
            postService.deletePostTag(memberInfo.getId(), tagId);
        }
        CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "deletePostTag", tagDTO);
        return ResponseEntity.ok(commonResponse);
    }







    // --- Post 응답 객체 캡슐화 ---
    @Getter
    @AllArgsConstructor
    private static class PostResponse{
        private List<PostDTO> postDTOs ;
    }

    // --- Post 요청 객체 캡슐화 ---
    @Getter
    @Setter
    private static class PostRequest {
        private String name ;
        private String contents;
        private int views;
        private int categoryId;
        private int userId;
        private int fileId;
        private Date updateTime;
    }

    @Getter
    @Setter
    private static class PostDeleteRequest {
        private int id ;
        private int accountId;
    }


}
