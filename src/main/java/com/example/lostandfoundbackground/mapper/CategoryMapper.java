package com.example.lostandfoundbackground.mapper;

import com.example.lostandfoundbackground.dto.CategoryDTO;
import com.example.lostandfoundbackground.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author archi
 */
@Mapper
public interface CategoryMapper {

    void add(Category c);

    List<Category> list();

    void update(CategoryDTO categoryDTO);

    void remove(String categoryName);
}
