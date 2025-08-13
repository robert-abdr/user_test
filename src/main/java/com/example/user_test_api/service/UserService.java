package com.example.user_test_api.service;

import com.example.user_test_api.dto.UserDtoToCreate;
import com.example.user_test_api.dto.UserOutputDto;
import com.example.user_test_api.dto.UserUpdateDto;
import java.util.UUID;

public interface UserService {
    UserOutputDto createNewUser(UserDtoToCreate userDto);

    UserOutputDto getUser(UUID uuid);

    UserOutputDto updateUser(UserUpdateDto userDto);

    void deleteUser(UUID uuid);
}
