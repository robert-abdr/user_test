package com.example.user_test_api.dto;

import com.example.user_test_api.model.UserRole;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    @NotNull(message = "Id must be not Blank")
    private UUID uuid;

    private String fullName;
    private String phoneNumber;
    private String avatarUrl;
    private UserRole role;
}
