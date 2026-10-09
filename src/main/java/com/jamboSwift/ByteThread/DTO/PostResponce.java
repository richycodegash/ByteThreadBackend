package com.jamboSwift.ByteThread.DTO;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostResponce(
       String status,
       String slug,
       String message,
       LocalDateTime dateTime
) {
    public static PostResponce fromSlug(String slug){
        return PostResponce.builder()
                .slug(slug)
                .dateTime(LocalDateTime.now())
                .status("Successful")
                .message("The post is now ready to view.")
                .build();
    }
}
