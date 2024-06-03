package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.CommentDTO;
import com.example.lostandfoundbackground.dto.Result;

/**
 * @author archi
 */
public interface CommentService {
    Result comment(int entityType, CommentDTO commentDTO);
    Result edit(int entityType, Long entityId,String content);

    Result findCommentByEntityId(int entityType, Long entityId,Long offset,Long limit);



}
