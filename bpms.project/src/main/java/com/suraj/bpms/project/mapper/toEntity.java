package com.suraj.bpms.project.mapper;

import com.suraj.bpms.project.dto.user.UserCreateDTO;
import com.suraj.bpms.project.entity.Role;
import com.suraj.bpms.project.entity.User;

public class toEntity {
    public User toEntity(UserCreateDTO userCreateDTO, Role role) {

        User user = new User();

        user.setName(userCreateDTO.getName());
        user.setEmail(userCreateDTO.getEmail());
        user.setRole(role);

        return user;
    }
}
