package com.example.lostandfoundbackground.mapper;

import com.example.lostandfoundbackground.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author archi
 */
@Mapper
public interface AdminMapper {
    public Admin findAdminBaseByPhone(String phone);

    public void addAdmin(@Param("admin")Admin admin);

    public void deleteAdminById(Long id);

    public void changePwd(Long id,String newPwd);

    public String getPwd(Long id);

    public void banAdminById(Long id);

}
