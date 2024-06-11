package com.example.lostandfoundbackground.mapper;

import com.example.lostandfoundbackground.dto.RegisterFormDTO;
import com.example.lostandfoundbackground.entity.LostItem;
import com.example.lostandfoundbackground.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author archi
 */
@Mapper
public interface UserMapper {

    User findUserBaseByPhone(String phone);

    void changePwd(Long id, String newPwd);

    void addUser(User user);

    void updateAvatar(Long id,String avatarUrl);

    void updateUserName(Long id, String newName);

    void changePwdByPhone(String phone,String newPwd);
}
