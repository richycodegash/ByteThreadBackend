package com.jamboSwift.ByteThread.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

public record UpdatePostRequest(

        @NotNull(message = "Client issue.Post id required.")
        UUID postId,

        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Account name required.")
        String accountName,

        Optional<String> content,
        Optional<String> excerpt
) {
}
