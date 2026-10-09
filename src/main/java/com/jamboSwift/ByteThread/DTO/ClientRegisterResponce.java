package com.jamboSwift.ByteThread.DTO;

import com.jamboSwift.ByteThread.Models.Client;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ClientRegisterResponce(
        String status,
        String name,
        String accountName,
        String message,
        LocalDateTime dateTime

) {
    public static ClientRegisterResponce fromClient(Client client){
        return ClientRegisterResponce.builder()
                .accountName(client.getAccount().getAccountName())
                .dateTime(LocalDateTime.now())
                .message("Successfully created account("+ client.getAccount().getAccountName()+")")
                .name(client.getFirstName()+" "+client.getSecondName())
                .status("Success")
                .build();
    }
}
