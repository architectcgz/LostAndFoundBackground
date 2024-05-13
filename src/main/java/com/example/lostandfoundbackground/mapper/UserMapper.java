package com.example.lostandfoundbackground.mapper;

import com.example.lostandfoundbackground.dto.RegisterFormDTO;
import com.example.lostandfoundbackground.entity.User;
import org.apache.ibatis.annotations.Mapper;

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
}
