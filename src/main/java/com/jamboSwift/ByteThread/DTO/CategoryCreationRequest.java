package com.jamboSwift.ByteThread.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreationRequest (
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Category name required")
        @Size(max = 20,min = 2,message = "Category name too long or short.")
        String categoryName,

        @NotBlank(message = "Description is required")
        @Size(min = 5,max = 100,message = "Description must range between 5 to 20 characters.")
        String description

) {}
