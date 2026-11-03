package com.jamboSwift.ByteThread.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record FollowRequest (
        @Email(message = "Invalid email")
        String email,

        @NotNull(message = "Client issue.account name required.")
        String accountToFollow
) {}
