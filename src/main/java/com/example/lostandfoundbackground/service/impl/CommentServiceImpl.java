package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.lostandfoundbackground.config.security.userDetails.SecurityAdminDetails;
import com.example.lostandfoundbackground.config.security.userDetails.SecurityUserDetails;
import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.CommentDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.Comment;
import com.example.lostandfoundbackground.mapper.CommentMapper;
import com.example.lostandfoundbackground.service.CommentService;
import com.example.lostandfoundbackground.utils.SecurityContextUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author archi
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Override
    public Result comment(int entityType, CommentDTO commentDTO) {
        Comment comment = BeanUtil.copyProperties(commentDTO,Comment.class);
        UserDetails nowUser = SecurityContextUtils.getLocalUserDetail();
        if(nowUser==null){
            return Result.error(HttpStatus.UNAUTHORIZED,"请先登录再进行操作!");
        }
        Long userId;
        if(nowUser instanceof SecurityUserDetails){
            userId = ((SecurityUserDetails) nowUser).getUser().getId();
            log.info("UserType = User");
            comment.setUserType(1);
        }else{
            log.info("UserType = Admin");
            userId = ((SecurityAdminDetails) nowUser).getAdmin().getId();
            comment.setUserType(2);
        }
        comment.setUserId(userId);
        comment.setStatus(0);
        comment.setEntityType(entityType);
        commentMapper.addComment(comment);
        return Result.ok();
    }

    @Override
    public Result edit(int entityType, Long entityId,String content) {
        commentMapper.editCommentByEntityId(entityType, entityId, content);
        return Result.ok();
    }

    @Override
    public Result findCommentByEntityId(int entityType, Long entityId,Long offset,Long limit) {
        List<Comment> commentList = commentMapper.findCommentByEntity(entityType,entityId,offset,limit);
        return Result.ok(commentList,(long)commentList.size());
    }
}
