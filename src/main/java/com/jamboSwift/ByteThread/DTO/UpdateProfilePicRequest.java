package com.jamboSwift.ByteThread.DTO;

import jakarta.validation.constraints.Email;

public record UpdateProfilePicRequest(
        @Email(message = "Invalid email")
        String email
) {
}
