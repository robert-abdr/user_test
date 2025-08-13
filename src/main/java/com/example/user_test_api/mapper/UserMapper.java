package com.example.user_test_api.mapper;

import com.example.user_test_api.dto.UserOutputDto;
import com.example.user_test_api.dto.UserUpdateDto;
import com.example.user_test_api.model.Role;
import com.example.user_test_api.model.User;
import com.example.user_test_api.model.UserRole;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);

    @Mapping(target = "role", source = "role", qualifiedByName = "mapRoleToUserRole")
    UserOutputDto toOutputDto(User user);

    @Named("mapRoleToUserRole")
    default UserRole mapRoleToUserRole(Role role) {
        if (role == null) {
            return null;
        }
        return role.getRole();
    }
}
