package com.lzy.bishe.modules.comment.model.entry;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Lzy
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Comment {

    @ApiModelProperty(notes = "主键id")
    public Integer commentId;

    @ApiModelProperty(notes = "帖子id")
    public Integer tieId;

    @ApiModelProperty(notes = "用户id")
    public Integer commentUserId;

    @ApiModelProperty(notes = "评论内容")
    public String commentContent;

    @ApiModelProperty(notes = "时间")
    public String commentTime;

    @ApiModelProperty(notes = "点赞数")
    public Integer commentLikes;

    @ApiModelProperty(notes = "评论类型")
    public Integer commentTypes;

    @ApiModelProperty(notes = "评论图片")
    public String commentPicture;

}
