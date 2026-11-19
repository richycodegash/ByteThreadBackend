package com.jamboSwift.ByteThread.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ShareRequest(
        @NotBlank(message = "sender account name is required")
        String senderAccount,

        @NotBlank(message = "receiver account name is required")
        String receiverAccount,

        @NotNull(message = "post is required")
        UUID postId
) {
}
