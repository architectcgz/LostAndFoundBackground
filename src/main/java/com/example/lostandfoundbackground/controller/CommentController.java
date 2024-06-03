package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.dto.CommentDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author archi
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    /**
     *
     * 根据评论目标（类别、id）对评论进行分页查询
     * //@param entityType
     * // 评论目标的类型（帖子、评论）
     * // 1表示对失物的帖子评论,2表示对招领的帖子的评论
     * // 3表示对失物帖子下面评论的评论,4表示对招领的帖子下面评论的评论
     * //@param entityId 评论目标的id
     * //@param offset 分页的起始点
     * //@param limit 页面中评论的数量
     * //@return 评论
     */

    @GetMapping("/lost/user/{entityId}")
    public Result getCommentToUserInLost(@PathVariable("entityId")Long entityId,
                                         @RequestParam("pageNum")Long pageNum,
                                         @RequestParam("pageSize")Long pageSize){
        return commentService.findCommentByEntityId(3,entityId,(pageNum-1)*pageSize,pageSize);
    }


    @GetMapping("/found/user/{entityId}")
    public Result getCommentToUserInFound(@PathVariable("entityId")Long entityId,
                                          @RequestParam("pageNum")Long pageNum,
                                          @RequestParam("pageSize")Long pageSize){
        return commentService.findCommentByEntityId(4,entityId,pageNum,pageSize);
    }

    @GetMapping("/lost/{entityId}")
    public Result getCommentInLostPart(@PathVariable("entityId")Long entityId,
                                       @RequestParam("pageNum")Long pageNum,
                                       @RequestParam("pageSize")Long pageSize){
        return commentService.findCommentByEntityId(1,entityId,(pageNum-1)*pageSize,pageSize);
    }

    @GetMapping("/found/{entityId}")
    public Result getCommentInFoundPart(@PathVariable("entityId")Long entityId,
                                        @RequestParam("pageNum")Long pageNum,
                                        @RequestParam("pageSize")Long pageSize){
        return commentService.findCommentByEntityId(2,entityId,(pageNum-1)*pageSize,pageSize);
    }

    @PostMapping("/lost/publish")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Result commentInLost(@Validated @RequestBody CommentDTO commentDTO){
        return commentService.comment(1,commentDTO);
    }

    @PostMapping("/found/publish")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Result commentInFound(@Validated @RequestBody CommentDTO commentDTO){
        return commentService.comment(2,commentDTO);
    }

    @PostMapping("/lost/user/publish")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Result commentInLostToUser(@Validated @RequestBody CommentDTO commentDTO){
        return commentService.comment(3,commentDTO);
    }

    @PostMapping("/found/user/publish")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Result commentInFoundToUser(@Validated @RequestBody CommentDTO commentDTO){
        return commentService.comment(4,commentDTO);
    }

    @PostMapping("/lost/edit/{entityId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Result editCommentInLost(@PathVariable("entityId")Long entityId,@RequestBody String content){
        return commentService.edit(1,entityId,content);
    }

    @PostMapping("/found/edit/{entityId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Result editCommentInFound(@PathVariable("entityId")Long entityId,@RequestBody String content){
        return commentService.edit(2,entityId,content);
    }

    @PostMapping("/lost/user/edit/{entityId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Result editCommentInLostToUser(@PathVariable("entityId")Long entityId,@RequestBody String content){
        return commentService.edit(3,entityId,content);
    }
    @PostMapping("/found/user/edit/{entityId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Result editCommentInFoundToUser(@PathVariable("entityId")Long entityId,@RequestBody String content){
        return commentService.edit(4,entityId,content);
    }
}
