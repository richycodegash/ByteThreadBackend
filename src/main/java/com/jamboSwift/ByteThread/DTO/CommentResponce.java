package com.jamboSwift.ByteThread.DTO;

import com.jamboSwift.ByteThread.Enums.CommentType;
import com.jamboSwift.ByteThread.Models.Comment;
import com.jamboSwift.ByteThread.Utilities.TimeConverter;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record CommentResponce (
        UUID commentId,
        String accountName,
        String profilPicUrl,
        String content,
        CommentType type,
        int replyCount,
        LocalDateTime creationDate

) {
    public static CommentResponce fromComment(Comment comment){
        System.out.println("**** inside from comment.");

        LocalDateTime creationDate= TimeConverter.toLocalDateTime(comment.getCreatedAt(),"Africa/Nairobi");

        /*List<CommentReplyObject> replies=comment.getReplies().stream().map(
                coment->

                        CommentReplyObject.builder()
                                .accountName(coment.getAccount().getAccountName())
                                .profilePicUrl(coment.getAccount().getAvatarUrl())
                                .creationDate(TimeConverter.toLocalDateTime(coment.getCreatedAt(),"Africa/Nairobi"))
                                .type(coment.getCommentType())
                                .content(coment.getContent()).build()
        ).toList();*/

        return CommentResponce.builder()
                .commentId(comment.getId())
                .accountName(comment.getAccount().getAccountName())
                .profilPicUrl(comment.getAccount().getAvatarUrl())
                .content(comment.getContent())
                .type(comment.getCommentType())
                .creationDate(creationDate)
                .replyCount(comment.getReplies().size())
                .build();
    }
}
