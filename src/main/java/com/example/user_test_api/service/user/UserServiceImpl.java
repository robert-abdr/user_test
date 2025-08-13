package com.example.user_test_api.service.user;

import com.example.user_test_api.dto.UserDtoToCreate;
import com.example.user_test_api.dto.UserOutputDto;
import com.example.user_test_api.dto.UserUpdateDto;
import com.example.user_test_api.mapper.UserMapper;
import com.example.user_test_api.model.Role;
import com.example.user_test_api.model.User;
import com.example.user_test_api.model.UserRole;
import com.example.user_test_api.repository.RoleRepository;
import com.example.user_test_api.repository.UserRepository;
import com.example.user_test_api.service.UserService;
import com.example.user_test_api.util.UserServiceValidator;
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
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String DEFAULT_AVATAR_URL_PREFIX = "https://api.dicebear.com/7.x/identicon/svg?seed=";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserServiceValidator userServiceValidator;

    @Override
    @Transactional
    @CachePut(value = "users", key = "#result.uuid")
    public UserOutputDto createNewUser(UserDtoToCreate userDto) {
        userServiceValidator.validateUserDto(userDto);

        String name = userDto.getFullName();
        String phoneNumber = userDto.getPhoneNumber();

        userServiceValidator.checkUserExists(name, phoneNumber, userRepository);

        log.info("Starting create new user with: name: {} and number: {}", name, phoneNumber);

        UserRole userRole = getUserRoleOrDefault(userDto.getRole());
        String avatarUrl = getAvatarUrlOrDefault(userDto.getAvatarUrl(), name);
        Role roleEntity = findOrCreateRole(userRole);
        User user = buildUser(name, phoneNumber, avatarUrl, roleEntity);
        User savedUser = userRepository.save(user);
        log.info("Successfully created user with UUID: {}", savedUser.getUuid());

        return userMapper.toOutputDto(savedUser);
    }

    @Override
    @Cacheable(value = "users", key = "#uuid")
    public UserOutputDto getUser(UUID uuid) {
        userServiceValidator.validateUuid(uuid);
        log.info("Searching for user with UUID: {}", uuid);
        User user = findUserByUuid(uuid);

        return userMapper.toOutputDto(user);
    }

    @Override
    @Transactional
    @CachePut(value = "users", key = "#result.uuid")
    public UserOutputDto updateUser(UserUpdateDto userDto) {
        userServiceValidator.validateUpdateDto(userDto);

        User user = findUserByUuid(userDto.getUuid());
        log.info("Starting update user with uuid: {}", userDto.getUuid());
        updateUserRole(user, userDto.getRole());

        userMapper.updateUserFromDto(userDto, user);

        User savedUser = userRepository.save(user);
        log.info("Successfully updated user with UUID: {}", savedUser.getUuid());

        return userMapper.toOutputDto(savedUser);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#uuid")
    public void deleteUser(UUID uuid) {
        userServiceValidator.validateUuid(uuid);

        User user = findUserByUuid(uuid);
        log.info("Starting delete user with uuid: {}", uuid);

        removeUserFromRole(user);

        userRepository.delete(user);
        log.info("Successfully deleted user with UUID: {}", uuid);
    }

    private UserRole getUserRoleOrDefault(UserRole role) {
        if (role == null) {
            log.debug("No role specified, using default role: {}", UserRole.MEMBER);
            return UserRole.MEMBER;
        }
        return role;
    }

    private String getAvatarUrlOrDefault(String avatarUrl, String name) {
        if (!StringUtils.hasText(avatarUrl)) {
            String defaultUrl = DEFAULT_AVATAR_URL_PREFIX + name;
            log.debug("No avatar URL specified, using default: {}", defaultUrl);
            return defaultUrl;
        }
        return avatarUrl;
    }

    private Role findOrCreateRole(UserRole userRole) {
        return roleRepository.findByRole(userRole).orElseGet(() -> createNewRole(userRole));
    }

    private Role createNewRole(UserRole userRole) {
        log.info("Creating new role: {}", userRole);
        Role role = new Role();
        role.setRole(userRole);
        role.setUsers(new ArrayList<>());
        return roleRepository.save(role);
    }

    private User findUserByUuid(UUID uuid) {
        return userRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User with UUID: %s not found".formatted(uuid)));
    }

    private User buildUser(String name, String phoneNumber, String avatarUrl, Role roleEntity) {
        User user = User.builder()
                .fullName(name)
                .phoneNumber(phoneNumber)
                .avatarUrl(avatarUrl)
                .role(roleEntity)
                .build();
        if (roleEntity.getUsers() == null) {
            roleEntity.setUsers(new ArrayList<>());
        }
        roleEntity.getUsers().add(user);

        return user;
    }

    private void updateUserRole(User user, UserRole newRole) {
        if (newRole != null && !newRole.equals(user.getRole().getRole())) {
            Role oldRole = user.getRole();
            Role newRoleEntity = findOrCreateRole(newRole);

            if (oldRole.getUsers() != null) {
                oldRole.getUsers().remove(user);
            }
            if (newRoleEntity.getUsers() == null) {
                newRoleEntity.setUsers(new ArrayList<>());
            }
            newRoleEntity.getUsers().add(user);

            user.setRole(newRoleEntity);
            log.debug("Updated user role from {} to: {}", oldRole.getRole(), newRole);
        }
    }

    private void removeUserFromRole(User user) {
        Role role = user.getRole();

        if (role != null && role.getUsers() != null) {
            role.getUsers().remove(user);

            log.debug("Removed user from role: {}", role.getRole());
        }
    }
}
