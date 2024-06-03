package com.example.lostandfoundbackground.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author archi
 */
@Data
@Getter
@Setter
public class CommentDTO {
    // 发布评论的作者 使用ThreadLocal获取
    //private Long userId;
    // 评论目标的类型（帖子、评论）
    // 1表示对失物的帖子评论,2表示对招领的帖子的评论
    // 3表示对失物帖子下面评论的评论,4表示对招领的帖子下面评论的评论
    //private int entityType;
    // 评论目标的 id
    @NotNull(message = "评论的帖子id不能为空")
    private Long entityId;
    // 指明对哪个用户进行评论(用户 id) 0表示对帖子进行评论
    @NotNull(message = "评论的对象id不能为空")
    private Long targetId;
    // 内容
    @NotBlank(message = "评论不能为空!")
    @Size(min = 1,max = 200,message = "评论字数在1-200之内")
    private String content;
}
