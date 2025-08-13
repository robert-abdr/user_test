package com.example.user_test_api.repository;

import com.example.user_test_api.model.Role;
import com.example.user_test_api.model.UserRole;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRole(UserRole role);
}
