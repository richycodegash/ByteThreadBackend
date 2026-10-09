package com.jamboSwift.ByteThread.DTO;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CategoryCreationResponce(
        String status,
        String message,
        LocalDateTime dateTime
) {
    public static CategoryCreationResponce fromCategory(String categoryName,String clientName){
        return CategoryCreationResponce.builder()
                .dateTime(LocalDateTime.now())
                .message("hello "+clientName+", you have created a new category ("+categoryName+")")
                .status("success")
                .build();
    }
}
