package com.jamboSwift.ByteThread.DTO;


import jakarta.validation.constraints.*;

public record ClientRegisterRequest(
        @NotBlank(message = "First name is required")
        @NotEmpty(message = "First name is required")
        @Size(max = 15,min = 3,message = "Invalid name.Name must range between 3-15 characters")
        String firstName,

        @NotBlank(message = "Second name is required")
        @Size(max = 15,min = 3,message = "Invalid name.Name must range between 3-15 characters")
        String secondName,

        @Email(message = "This email seems invalid")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Phone number is required.")
        @Pattern(
                regexp = "^(?:0[71][0-9]{8}|(?:\\+?254)[71][0-9]{8})$",
                message = "invalid phone number"
        )
        String phoneNumber,

        @NotBlank(message = "Password is required")
        @Size(min = 4,message = "Weak security.Password must have more than 4 characters.")
        String password

) {
}
