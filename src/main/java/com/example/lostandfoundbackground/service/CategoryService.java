package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.CategoryDTO;
import com.example.lostandfoundbackground.dto.Result;

/**
 * @author archi
 */
public interface CategoryService {
    Result add(CategoryDTO categoryDTO);

    Result list();

    Result update(CategoryDTO categoryDTO);

    Result delete(String categoryName);

}
