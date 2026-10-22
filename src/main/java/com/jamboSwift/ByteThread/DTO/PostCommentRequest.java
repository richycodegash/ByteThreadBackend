package com.jamboSwift.ByteThread.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record PostCommentRequest(
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "comment is required")
        String content,

        @NotBlank(message = "post is required")
        UUID postId,

        UUID parentCommentId



) {}
