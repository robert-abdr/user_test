package com.example.user_test_api.dto;

import com.example.user_test_api.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoToCreate {
    @NotBlank(message = "Name can not be null")
    private String fullName;

    @NotBlank(message = "Number can not be null")
    @Size(min = 10, max = 17, message = "Invalid phone number")
    private String phoneNumber;

    private String avatarUrl;
    private UserRole role;
}
