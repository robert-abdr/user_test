package com.example.user_test_api.repository;

import com.example.user_test_api.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUuid(UUID id);

    Optional<User> findByFullNameAndPhoneNumber(String fullName, String phoneNumber);
}
