package com.example.user_test_api.controller.user;

import com.example.user_test_api.dto.UserDtoToCreate;
import com.example.user_test_api.dto.UserOutputDto;
import com.example.user_test_api.dto.UserUpdateDto;
import com.example.user_test_api.service.UserService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("${controller.base_mapping}")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/createNewUser")
    public ResponseEntity<UserOutputDto> createNewUser(@Valid @RequestBody UserDtoToCreate userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createNewUser(userDto));
    }

    @GetMapping("users")
    public ResponseEntity<UserOutputDto> getUser(@RequestParam("userID") UUID uuid) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(uuid));
    }

    @PutMapping("/userDetailsUpdate")
    public ResponseEntity<UserOutputDto> updateUser(@Valid @RequestBody UserUpdateDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto));
    }

    @DeleteMapping("users")
    public ResponseEntity<Void> deleteUser(@RequestParam("userID") UUID uuid) {
        userService.deleteUser(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
