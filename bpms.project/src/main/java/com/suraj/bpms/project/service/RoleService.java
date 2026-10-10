package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.role.RoleResponseDTO;
import com.suraj.bpms.project.mapper.RoleMapper;
import com.suraj.bpms.project.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponseDTO)
                .toList();
    }
}
