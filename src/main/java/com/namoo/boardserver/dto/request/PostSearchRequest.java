package com.namoo.boardserver.dto.request;

import com.namoo.boardserver.dto.CategoryDTO;
import com.namoo.boardserver.dto.SortStatus;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchRequest {
    private int id;
    private String name;
    private String contents;
    private int views;
    private int categoryId;
    private SortStatus sortStatus;
}
