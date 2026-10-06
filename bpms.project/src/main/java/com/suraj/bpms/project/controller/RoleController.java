package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.entity.Role;
import com.suraj.bpms.project.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @GetMapping("/api/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }
}
