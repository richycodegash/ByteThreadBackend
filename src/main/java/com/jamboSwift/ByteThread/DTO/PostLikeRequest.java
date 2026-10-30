package com.jamboSwift.ByteThread.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record PostLikeRequest(
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "post is required")
        UUID postId
) {
}
