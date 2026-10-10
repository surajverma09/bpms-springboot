    package com.suraj.bpms.project.controller;

    import com.suraj.bpms.project.dto.role.RoleResponseDTO;
    import com.suraj.bpms.project.service.RoleService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RestController;

    import java.util.List;

    @RequiredArgsConstructor
    @RestController
    public class RoleController {

        private final RoleService roleService;

        @GetMapping("/api/roles")
        public List<RoleResponseDTO> getAllRoles() {
            return roleService.getAllRoles();
        }
    }
