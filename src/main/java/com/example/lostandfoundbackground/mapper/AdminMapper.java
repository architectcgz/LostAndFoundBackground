package com.example.lostandfoundbackground.mapper;

import com.example.lostandfoundbackground.entity.Admin;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author archi
 */
@Mapper
public interface AdminMapper {
    Admin findAdminBaseByPhone(String phone);

    void addAdmin(Admin admin);

    void deleteAdminById(Long id);

    void changePwd(Long id,String newPwd);

    String getPwd(Long id);

    void banAdminById(Long id);

}
