package com.example.lostandfoundbackground.mapper;

import com.example.lostandfoundbackground.entity.Administrator;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    public Administrator findAdminBaseByPhone(String phone);

    public Administrator createAdmin(Administrator admin);
}
