package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.example.lostandfoundbackground.config.security.userDetails.SecurityAdminDetails;
import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.CategoryDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.dto.UserDTO;
import com.example.lostandfoundbackground.entity.Admin;
import com.example.lostandfoundbackground.entity.Category;
import com.example.lostandfoundbackground.mapper.CategoryMapper;
import com.example.lostandfoundbackground.service.CategoryService;
import com.example.lostandfoundbackground.utils.SecurityContextUtils;
import com.example.lostandfoundbackground.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

import static com.example.lostandfoundbackground.utils.ValidateUtils.validateCategoryDTO;

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
        SecurityAdminDetails adminDetails = (SecurityAdminDetails) SecurityContextUtils.getLocalUserDetail();
        if(adminDetails==null){
            return Result.error(HttpStatus.UNAUTHORIZED,"请登录后再进行该操作!");
        }
        Map<String,Object>resultMap = validateCategoryDTO(categoryDTO);
        if(!((boolean) resultMap.get("valid"))){
            return Result.error(1, (String) resultMap.get("msg"));
        }
        Admin nowUser = adminDetails.getAdmin();
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
        return Result.ok(categoryList,(long)categoryList.size());
    }

    @Override
    public Result update(CategoryDTO categoryDTO) {
        SecurityAdminDetails adminDetails = (SecurityAdminDetails) SecurityContextUtils.getLocalUserDetail();
        if(adminDetails==null){
            return Result.error(HttpStatus.UNAUTHORIZED,"请登录后再进行该操作!");
        }
        Map<String,Object>resultMap = validateCategoryDTO(categoryDTO);
        if(!((boolean) resultMap.get("valid"))){
            return Result.error(1, (String) resultMap.get("msg"));
        }
        Admin nowUser = adminDetails.getAdmin();
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
