package com.jamboSwift.ByteThread.DTO;

import com.jamboSwift.ByteThread.Enums.NotificationSubject;
import com.jamboSwift.ByteThread.Models.Notification;
import com.jamboSwift.ByteThread.Models.Post;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record NotificationResponce(
        String message,
        NotificationSubject subject,
        Instant instance,
        UUID postId
) {
    public static NotificationResponce fromNotification(Notification notification){
        UUID postid=null;
        Post post=notification.getPost();
        if (post != null){
            postid=post.getId();
        }

        return NotificationResponce.builder()
                .instance(notification.getCreatedAt())
                .message(notification.getMessage())
                .postId(postid)
                .subject(notification.getSubject())
                .build();
    }
}
