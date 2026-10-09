package com.jamboSwift.ByteThread.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangePassworRequest (
        @NotBlank(message = "email is required")
        @Email()
        String email,

        @NotBlank(message = "old password is required")
        String oldPassword,

        @NotBlank(message = "new password is required")
        String newPassword

) {}
