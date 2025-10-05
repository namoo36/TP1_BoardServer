package com.namoo.boardserver.mapper;

import com.namoo.boardserver.dto.PostDTO;
import com.namoo.boardserver.dto.request.PostSearchRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostSearchMapper {
    public List<PostDTO> selectPosts(PostSearchRequest postSearchRequest);
}
