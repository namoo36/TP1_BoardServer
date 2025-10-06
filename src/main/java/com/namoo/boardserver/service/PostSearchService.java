package com.namoo.boardserver.service;

import com.namoo.boardserver.dto.PostDTO;
import com.namoo.boardserver.dto.request.PostSearchRequest;

import java.util.List;

public interface PostSearchService {
    List<PostDTO> getPosts(PostSearchRequest postSearchRequest);
    List<PostDTO> getPostsByTag(String tagName);

}
