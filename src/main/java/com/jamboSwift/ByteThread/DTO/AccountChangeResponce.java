package com.jamboSwift.ByteThread.DTO;

import com.jamboSwift.ByteThread.Models.Account;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AccountChangeResponce(
        String status,
        String accountName,
        LocalDateTime dateTime
) {
    public static AccountChangeResponce fromAccount(Account account){
        return AccountChangeResponce.builder()
                .accountName(account.getAccountName())
                .status("success")
                .dateTime(LocalDateTime.now())
                .build();
    }
}
