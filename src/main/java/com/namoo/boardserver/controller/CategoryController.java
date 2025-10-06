package com.namoo.boardserver.controller;

import com.namoo.boardserver.aop.LoginCheck;
import com.namoo.boardserver.dto.CategoryDTO;
import com.namoo.boardserver.dto.SortStatus;
import com.namoo.boardserver.service.impl.CategoryServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categories")
@Log4j2
@AllArgsConstructor
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.ADMIN)   // 관리자만 카테고리 수정이 가능함
    public void registerCategory(String accountId, @RequestBody CategoryDTO categoryDTO){
        categoryService.register(accountId, categoryDTO);
    }

    @PatchMapping("{categoryId}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)   // 관리자만 카테고리 수정이 가능함
    public void updateCategories(String accountId,
                                 @PathVariable(name="categoryId") int categoryId,
                                 @RequestBody CategoryRequest categoryRequest){
        CategoryDTO categoryDTO = new CategoryDTO(categoryId, categoryRequest.getName(), SortStatus.NEWEST, 10, 1 );
        categoryService.update(categoryDTO);

    }

    @DeleteMapping("{categoryId}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)   // 관리자만 카테고리 수정이 가능함
    public void deleteCategories(String accountId,
                                 @PathVariable(name="categoryId") int categoryId){
        categoryService.delete(categoryId);

    }

    // --- request 객체 ---
    @Getter
    @Setter
    private static class CategoryRequest{
        private int id;
        private String name;
    }

}
