package com.example.lostandfoundbackground.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.example.lostandfoundbackground.dto.CategoryDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.dto.UserDTO;
import com.example.lostandfoundbackground.entity.Category;
import com.example.lostandfoundbackground.service.CategoryService;
import com.example.lostandfoundbackground.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author archi
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @PostMapping("/add")
    Result add(CategoryDTO categoryDTO){
        return categoryService.add(categoryDTO);
    }

    @GetMapping("/list")
    Result list(){
        return categoryService.list();
    }

    @PostMapping("/update")
    Result update(CategoryDTO categoryDTO){
        return categoryService.update(categoryDTO);
    }

    @PostMapping("/delete")
    Result delete(@RequestParam("name")String categoryName){
        return categoryService.delete(categoryName);
    }
}
