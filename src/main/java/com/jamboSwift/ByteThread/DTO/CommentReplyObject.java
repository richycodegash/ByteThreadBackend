package com.jamboSwift.ByteThread.DTO;

import com.jamboSwift.ByteThread.Enums.CommentType;
import com.jamboSwift.ByteThread.Models.Comment;
import com.jamboSwift.ByteThread.Utilities.TimeConverter;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentReplyObject(
        String accountName,
        String profilePicUrl,
        String content,
        CommentType type,
        LocalDateTime creationDate

) {
    public static CommentReplyObject fromReply(Comment reply){
        LocalDateTime creationDate= TimeConverter.toLocalDateTime(reply.getCreatedAt(),"Africa/Nairobi");

        return CommentReplyObject.builder()
                .type(reply.getCommentType())
                .content(reply.getContent())
                .creationDate(creationDate)
                .profilePicUrl(reply.getAccount().getAvatarUrl())
                .accountName(reply.getAccount().getAccountName())
                .build();
    }
}
