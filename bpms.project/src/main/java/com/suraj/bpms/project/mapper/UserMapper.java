package com.suraj.bpms.project.mapper;

import com.suraj.bpms.project.dto.user.RoleResponseDTO;
import com.suraj.bpms.project.dto.user.UserCreateDTO;
import com.suraj.bpms.project.dto.user.UserResponseDTO;
import com.suraj.bpms.project.entity.Role;
import com.suraj.bpms.project.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toResponseDTO(User user) {

        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(
                user.getRole().getId(),
                user.getRole().getName()
        );

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roleResponseDTO,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
    public User toEntity(UserCreateDTO userCreateDTO, Role role) {

        User user = new User();

        user.setName(userCreateDTO.getName());
        user.setEmail(userCreateDTO.getEmail());
        user.setRole(role);

        return user;
    }
}
