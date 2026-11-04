package com.jamboSwift.ByteThread.DTO;

import com.jamboSwift.ByteThread.Models.Account;
import lombok.Builder;

@Builder
public record AccountDetails(
        String accountName,
        String profilePic,
        String bio,
        Long followersCount,
        Long followingCount
) {
    public static AccountDetails fromAccount(Account account){
        return AccountDetails.builder()
                .accountName(account.getAccountName())
                .bio(account.getBio())
                .followersCount(account.getFollowersCountCount())
                .followingCount(account.getFollowingCountCount())
                .profilePic(account.getAvatarUrl())
                .build();
    }
}
