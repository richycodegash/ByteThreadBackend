package com.jamboSwift.ByteThread.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record PostRequest(
        @NotBlank(message = "Title is required")
        @Size(max=100,message = "title too long")
        String title,

        @NotBlank(message = "Content is required")
        String content,

        @NotBlank(message = "Excerpt required")
        @Size(max = 100,message = "Excerpt too long.")
        String excerpt,

        @NotBlank(message = "Account name is required")
        String accountName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email not valid")
        String email,

        Set<String> tags,

        Set<String> categories

) {}
