package com.jamboSwift.ByteThread.DTO;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChangePasswordResponce (
        String status,
        String message,
        LocalDateTime dateTime
){
    public static ChangePasswordResponce buildResponce(){
        return ChangePasswordResponce.builder()
                .message("Password changed successfully.")
                .dateTime(LocalDateTime.now())
                .status("Success")
                .build();
    }
}
