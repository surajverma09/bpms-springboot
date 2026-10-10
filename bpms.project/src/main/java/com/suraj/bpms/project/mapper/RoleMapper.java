package com.suraj.bpms.project.mapper;

import com.suraj.bpms.project.dto.role.RoleResponseDTO;
import com.suraj.bpms.project.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleResponseDTO toRoleResponseDTO(Role role){
        return new RoleResponseDTO(
                role.getId(),
                role.getName()
        );
    }

}
