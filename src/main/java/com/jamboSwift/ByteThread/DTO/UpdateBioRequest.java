package com.jamboSwift.ByteThread.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateBioRequest(
        @Email(message = "invalid email format")
        String email,

        @NotBlank(message = "bio text required")
        String bio
) {
}
