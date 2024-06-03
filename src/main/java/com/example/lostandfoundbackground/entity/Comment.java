package com.example.lostandfoundbackground.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author archi
 */
@Data
@Getter
@Setter
public class Comment {
    private Long id;
    //评论作者的类型 user或admin
    private int userType;
    // 发布评论的作者
    private Long userId;
    // 评论目标的类型（帖子、评论）
    // 1表示对失物的帖子评论,2表示对招领的帖子的评论
    // 3表示对失物帖子下面评论的评论,4表示对招领的帖子下面评论的评论
    private int entityType;
    // 评论目标的 id
    private Long entityId;
    // 指明对哪个用户进行评论(用户 id)
    private Long targetId;
    // 内容
    private String content;
    // 状态：0 正常，1 禁用
    private int status;
    // 发布时间
    private LocalDateTime createTime;
    //编辑(修改)时间
    private LocalDateTime updateTime;
}
