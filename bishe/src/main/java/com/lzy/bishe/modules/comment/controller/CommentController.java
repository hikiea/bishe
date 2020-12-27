package com.lzy.bishe.modules.comment.controller;

import com.lzy.bishe.annotation.UserLoginToken;
import com.lzy.bishe.modules.comment.model.entry.Comment;
import com.lzy.bishe.modules.comment.model.entry.SecondComment;
import com.lzy.bishe.modules.comment.model.entry.V_CommentUser;
import com.lzy.bishe.modules.comment.service.CommentService;
import com.lzy.bishe.modules.notify.service.NotifyService;
import com.lzy.bishe.modules.tie.mapper.TieDao;
import com.lzy.bishe.modules.tie.model.entry.V_TieUser;
import com.lzy.bishe.util.JWTInfo;
import com.lzy.bishe.util.ResultDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


/**
 * @author Lzy
 */
@RestController
@RequestMapping("/api/comment")
@Api(tags = {"CommentController"}, description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private TieDao tieDao;

    /* 发表一级评论 */
    @PostMapping("/first/comment")
    @UserLoginToken
    @CrossOrigin
    public ResultDTO doPublishComment(@RequestBody Comment comment,
                                      HttpServletRequest httpServletRequest){
        V_TieUser tie = tieDao.selectOneTie(comment.getTieId());
        notifyService.send(
                httpServletRequest,
                JWTInfo.getUserName(httpServletRequest)+" 评论了你：" + comment.getCommentContent(),
                tie.getUserId());
        ResultDTO resultDTO = commentService.doPublishComment(comment);
        return resultDTO;
    }

    /* 查看一级评论 */
    @GetMapping("/first/comment/{tieId}")
    @UserLoginToken @CrossOrigin
    public ResultDTO tieComment(@PathVariable("tieId") Integer tieId){
        ResultDTO resultDTO = commentService.selectTeiComment(tieId);
        return resultDTO;
    }

    /* 删除一级评论,二级评论 */
    @DeleteMapping("/comment/{commentId}")
    @UserLoginToken @CrossOrigin
    public ResultDTO deDeleteTieComment(@PathVariable("commentId") Integer commentId){
        ResultDTO resultDTO = commentService.deleteTieComment(commentId);
        return resultDTO;
    }

    /*发布二级评论*/
    @PostMapping("/second/comment")
    @UserLoginToken @CrossOrigin
    public ResultDTO doPublishSecondComment(@RequestBody SecondComment secondComment,
                                            HttpServletRequest httpServletRequest){
        V_CommentUser info = commentService.selectByComplaintId(secondComment.getCommentId());
        notifyService.send(
                httpServletRequest,
                JWTInfo.getUserName(httpServletRequest)+" 评论了你：" + secondComment.getCommentContent(),
                info.getCommentId());
        ResultDTO resultDTO = commentService.doPublishSecondComment(secondComment);
        return resultDTO;
    }

    /* 查询一条评论的二级评论 */
    @GetMapping("/second/comment/{replyCommentId}")
    @UserLoginToken @CrossOrigin
    public ResultDTO doSelectSecondComment(@PathVariable("replyCommentId") Integer replyCommentId){
        ResultDTO resultDTO = commentService.doSelectSecondComment(replyCommentId);
        return resultDTO;
    }

    /* 点赞一级评论 */
    @PutMapping("/like/comment/{commentId}")
    @UserLoginToken @CrossOrigin
    public ResultDTO doLikeComment(@PathVariable("commentId") Integer commentId) {
        ResultDTO resultDTO = commentService.likeComment(commentId);
        return resultDTO;
    }

    /* 取消点赞一级评论 */
    @PutMapping("/notLike/comment/{commentId}")
    @UserLoginToken @CrossOrigin
    public ResultDTO doNotLikeComment(@PathVariable("commentId") Integer commentId) {
        ResultDTO resultDTO = commentService.notLikeComment(commentId);
        return resultDTO;
    }
}
