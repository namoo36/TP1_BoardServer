package com.namoo.boardserver.service.impl;

import com.namoo.boardserver.dto.CategoryDTO;
import com.namoo.boardserver.mapper.CategoryMapper;
import com.namoo.boardserver.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void register(String accountId, CategoryDTO categoryDTO) {
        // 정상적으로 로그인 되었을 때
        if(accountId != null){
            try{
                categoryMapper.register(categoryDTO);
            } catch (RuntimeException e){
                log.error("registerId Error! {}", categoryDTO);
                throw new RuntimeException("registerId Error! 게시글 카테고리 등록 메서드를 확인해주세요.");
            }
        } else {
            log.error("registerId Error! {}", categoryDTO);
            throw new RuntimeException("registerId Error! 게시글 카테고리 등록 메서드를 확인해주세요.");
        }
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        if(categoryDTO != null){
            try{
                categoryMapper.updateCategory(categoryDTO);
            } catch (RuntimeException e){
                log.error("updateId Error! {}", categoryDTO);
                throw new RuntimeException("registerId Error! 게시글 카테고리 수정 메서드를 확인해주세요.");
            }
        } else{
            log.error("updateId Error! {}", categoryDTO);
            throw new RuntimeException("registerId Error! 게시글 카테고리 수정 메서드를 확인해주세요.");
        }
    }

    @Override
    public void delete(int categoryId) {
        if(categoryId != 0){
            try{
                categoryMapper.deleteCategory(categoryId);
            } catch (RuntimeException e){
                log.error("deleteId Error! {}", categoryId);
                throw new RuntimeException("registerId Error! 게시글 카테고리 삭제 메서드를 확인해주세요.");
            }
            categoryMapper.deleteCategory(categoryId);
        } else {
            log.error("deleteId Error! {}", categoryId);
            throw new RuntimeException("registerId Error! 게시글 카테고리 삭제 메서드를 확인해주세요.");

        }
    }
}
