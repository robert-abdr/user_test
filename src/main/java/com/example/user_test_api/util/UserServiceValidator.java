package com.example.user_test_api.util;

import com.example.user_test_api.dto.UserDtoToCreate;
import com.example.user_test_api.dto.UserUpdateDto;
import com.example.user_test_api.exception.UserExistException;
import com.example.user_test_api.repository.UserRepository;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class UserServiceValidator {
    public void validateUuid(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
    }

    public void validateUserDto(UserDtoToCreate userDto) {
        if (userDto == null) {
            throw new IllegalArgumentException("User DTO cannot be null");
        }
        if (!StringUtils.hasText(userDto.getFullName())) {
            throw new IllegalArgumentException("User full name cannot be empty");
        }
        if (!StringUtils.hasText(userDto.getPhoneNumber())) {
            throw new IllegalArgumentException("User phone number cannot be empty");
        }
    }

    public void validateUpdateDto(UserUpdateDto userDto) {
        if (userDto == null) {
            throw new IllegalArgumentException("Update DTO cannot be null");
        }
        validateUuid(userDto.getUuid());
    }

    public void checkUserExists(String name, String phoneNumber, UserRepository userRepository) {
        if (userRepository.findByFullNameAndPhoneNumber(name, phoneNumber).isPresent()) {
            String errorMessage = "User with name: %s and phone number: %s already exists".formatted(name, phoneNumber);
            log.error(errorMessage);
            throw new UserExistException(errorMessage);
        }
    }
}
