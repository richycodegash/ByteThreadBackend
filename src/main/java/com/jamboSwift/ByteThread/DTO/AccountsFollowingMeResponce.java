package com.jamboSwift.ByteThread.DTO;

import com.jamboSwift.ByteThread.Models.Account;
import lombok.Builder;

@Builder
public record AccountsFollowingMeResponce(
        String accountName,
        String profilePicUrl,
        String firstName,
        String lastName
) {
    public static AccountsFollowingMeResponce fromAccount(Account account){
        return AccountsFollowingMeResponce.builder()
                .accountName(account.getAccountName())
                .profilePicUrl(account.getAvatarUrl())
                .firstName(account.getClient().getFirstName())
                .lastName(account.getClient().getSecondName())
                .build();
    }
}
