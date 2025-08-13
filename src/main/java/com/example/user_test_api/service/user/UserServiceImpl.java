package com.example.user_test_api.service.user;

import com.example.user_test_api.dto.UserDtoToCreate;
import com.example.user_test_api.dto.UserOutputDto;
import com.example.user_test_api.dto.UserUpdateDto;
import com.example.user_test_api.exception.UserExistException;
import com.example.user_test_api.mapper.UserMapper;
import com.example.user_test_api.model.Role;
import com.example.user_test_api.model.User;
import com.example.user_test_api.model.UserRole;
import com.example.user_test_api.repository.RoleRepository;
import com.example.user_test_api.repository.UserRepository;
import com.example.user_test_api.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String DEFAULT_AVATAR_URL_PREFIX = "https://api.dicebear.com/7.x/identicon/svg?seed=";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @CachePut(value = "users", key = "#result.uuid")
    public UserOutputDto createNewUser(UserDtoToCreate userDto) {
        String name = userDto.getFullName();
        String phoneNumber = userDto.getPhoneNumber();

        if (userRepository.findByFullNameAndPhoneNumber(name, phoneNumber).isPresent()) {
            String errorMessage = "User with name: %s and phone number %s already exist".formatted(name, phoneNumber);
            log.error(errorMessage);
            throw new UserExistException(errorMessage);
        }

        log.info("Starting create new user with: name: {} and number: {}", name, phoneNumber);

        UserRole role = userDto.getRole();
        String avatarUrl = userDto.getAvatarUrl();

        if (role == null) {
            role = UserRole.MEMBER;
            log.info("The user with name: {} and number: {} has a default role set: {}", name, phoneNumber, role);
        }

        if (avatarUrl == null) {
            avatarUrl = (DEFAULT_AVATAR_URL_PREFIX + name);
            log.info(
                    "The user with name: {} and number: {} has a default avatar set: {}", name, phoneNumber, avatarUrl);
        }

        Role roleEntity = roleRepository.findByRole(role).orElse(null);
        if (roleEntity == null) {
            roleEntity = new Role();
            roleEntity.setRole(role);
            roleEntity.setUsers(new ArrayList<>());
            roleRepository.save(roleEntity);
            log.info("New role: {} was added in database", role);
        }

        User user = User.builder()
                .fullName(name)
                .phoneNumber(phoneNumber)
                .avatarUrl(avatarUrl)
                .role(roleEntity)
                .build();

        if (roleEntity.getUsers() == null) {
            log.info("Empty userlist for role: {}, add new userlist", roleEntity);
            roleEntity.setUsers(new ArrayList<>());
        }
        roleEntity.getUsers().add(user);
        roleRepository.save(roleEntity);

        return userMapper.toOutputDto(userRepository.save(user));
    }

    @Override
    @Cacheable(value = "users", key = "#uuid")
    public UserOutputDto getUser(UUID uuid) {
        log.info("Starting search user with uuid: {}", uuid);
        User user = userRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User with uuid: %s doesn't exist".formatted(uuid)));

        return userMapper.toOutputDto(user);
    }

    @Override
    @Transactional
    @CachePut(value = "users", key = "#result.uuid")
    public UserOutputDto updateUser(UserUpdateDto userDto) {
        User user = userRepository
                .findByUuid(userDto.getUuid())
                .orElseThrow(() ->
                        new EntityNotFoundException("User with uuid: %s doesn't found".formatted(userDto.getUuid())));
        log.info("Starting update user with uuid: {}", userDto.getUuid());
        UserRole userRole = userDto.getRole();
        if (userRole != null) {
            Role role = roleRepository.findByRole(userRole).orElse(null);
            if (role == null) {
                role = new Role();
                role.setRole(userRole);
                role.setUsers(new ArrayList<>());
                roleRepository.save(role);
            }
            user.setRole(role);
        }

        if (userDto.getFullName() == null) {
            userDto.setFullName(user.getFullName());
        }
        if (userDto.getPhoneNumber() == null) {
            userDto.setPhoneNumber(user.getPhoneNumber());
        }
        if (userDto.getAvatarUrl() == null) {
            userDto.setAvatarUrl(user.getAvatarUrl());
        }

        userMapper.updateUserFromDto(userDto, user);
        userRepository.save(user);
        return userMapper.toOutputDto(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#uuid")
    public void deleteUser(UUID uuid) {
        User user = userRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User with uuid: %s was not found".formatted(uuid)));
        log.info("Starting delete user with uuid: {}", uuid);

        Role role = user.getRole();
        if (role.getUsers() != null) {
            role.getUsers().remove(user);
        }
        userRepository.delete(user);
    }
}
