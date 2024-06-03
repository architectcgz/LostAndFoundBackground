package com.example.lostandfoundbackground.mapper;

import com.example.lostandfoundbackground.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author archi
 */
@Mapper
public interface CommentMapper {
    /**
     * 添加评论到数据库
     * @param comment 要添加的评论
     */
    void addComment(Comment comment);

    void editCommentByEntityId(int entityType,Long entityId, String content);

    /**
     *
     * 根据评论目标（类别、id）对评论进行分页查询
     * @param entityType
     * // 评论目标的类型（帖子、评论）
     * // 1表示对失物的帖子评论,2表示对招领的帖子的评论
     * // 3表示对失物帖子下面评论的评论,4表示对招领的帖子下面评论的评论
     * @param entityId 评论目标的id
     * @param offset 分页的起始点
     * @param limit 页面中评论的数量
     * @return 评论
     */
    List<Comment> findCommentByEntity(int entityType,Long entityId,Long offset,Long limit);

    /**
     * 通过id查找评论
     * @param id 评论的id
     * @return 评论
     */
    Comment findCommentById(Long id);

    /**
     * 查找某种类型评论的数量
     * @param entityType 评论的类型
     * @param entityId 评论目标的id
     * @return
     */
    public Long findCommentCount(int entityType,Long entityId);

    /**
     * 分页查询某个用户的评论/回复列表
     * @param userId 用户id
     * @param offset 分页起始处
     * @param limit 页面中评论数量
     * @return 评论列表
     */
    public List<Comment> findCommentByUserId(int userType,Long userId, Long offset, Long limit);


}
