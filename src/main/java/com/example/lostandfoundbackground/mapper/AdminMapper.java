package com.example.lostandfoundbackground.mapper;

import com.example.lostandfoundbackground.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {
    public Admin findAdminBaseByPhone(String phone);

    public void addAdmin(@Param("admin")Admin admin);

    public void changePwd(Long id,String newPwd);

}
