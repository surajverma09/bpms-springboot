package com.suraj.bpms.project.dto.user;

import com.suraj.bpms.project.dto.role.RoleResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private RoleResponseDTO role;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
