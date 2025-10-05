package com.namoo.boardserver.service.impl;

import com.namoo.boardserver.dto.PostDTO;
import com.namoo.boardserver.dto.request.PostSearchRequest;
import com.namoo.boardserver.mapper.PostSearchMapper;
import com.namoo.boardserver.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    private final PostSearchMapper postSearchMapper;


    @Cacheable(value = "getPosts", key = "'getPosts' + #postSearchRequest.getName() + #postSearchRequest.getCategoryId()")
    @Override
    public List<PostDTO> getPosts(PostSearchRequest postSearchRequest) {
        List<PostDTO> postDTOList = null;

        try{
            postDTOList = postSearchMapper.selectPosts(postSearchRequest);
        } catch (RuntimeException e){
            log.error("SelectPosts 메서드 실패",e.getMessage());
        }
        return postDTOList;
    }
}
