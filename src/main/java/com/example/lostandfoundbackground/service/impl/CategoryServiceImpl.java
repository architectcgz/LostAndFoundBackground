package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.example.lostandfoundbackground.dto.CategoryDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.dto.UserDTO;
import com.example.lostandfoundbackground.entity.Category;
import com.example.lostandfoundbackground.mapper.CategoryMapper;
import com.example.lostandfoundbackground.service.CategoryService;
import com.example.lostandfoundbackground.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author archi
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Result add(CategoryDTO categoryDTO) {
        Category c = BeanUtil.copyProperties(categoryDTO, Category.class);
        UserDTO nowUser = ThreadLocalUtil.get();
        c.setCreateUser(nowUser.getId());
        c.setUpdateUser(nowUser.getId());
        c.setCreateTime(DateTime.now());
        c.setUpdateTime(DateTime.now());
        categoryMapper.add(c);
        return Result.ok();
    }

    @Override
    public Result list() {
        List<Category> categoryList = categoryMapper.list();
        return Result.ok(categoryList);
    }

    @Override
    public Result update(CategoryDTO categoryDTO) {
        UserDTO nowUser = ThreadLocalUtil.get();
        categoryDTO.setUpdateUser(nowUser.getId());
        categoryMapper.update(categoryDTO);
        return Result.ok();
    }

    @Override
    public Result delete(String categoryName) {
        categoryMapper.remove(categoryName);
        return Result.ok();
    }
}
