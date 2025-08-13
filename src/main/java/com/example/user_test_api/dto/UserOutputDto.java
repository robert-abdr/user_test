package com.example.user_test_api.dto;

import com.example.user_test_api.model.UserRole;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOutputDto {
    private UUID uuid;
    private String fullName;
    private String phoneNumber;
    private String avatarUrl;
    private UserRole role;
}
