package com.example.user_test_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "full_name", nullable = false, length = 64)
    @NotBlank(message = "Name can not be blank")
    private String fullName;

    @NotBlank(message = "Phone number can not be empty")
    @Column(name = "phone_number", nullable = false, length = 17)
    private String phoneNumber;

    @URL(message = "Avatar must be valid URL")
    @Column(name = "avatar_url")
    private String avatarUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id")
    @NotNull(message = "Role must be not null")
    private Role role;
}
