package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.user.UserCreateDTO;
import com.suraj.bpms.project.dto.user.UserResponseDTO;
import com.suraj.bpms.project.dto.user.UserUpdateDTO;
import com.suraj.bpms.project.entity.Role;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.RoleNotFoundException;
import com.suraj.bpms.project.exception.UserNotFoundException;
import com.suraj.bpms.project.mapper.UserMapper;
import com.suraj.bpms.project.repository.RoleRepository;
import com.suraj.bpms.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public void createUser(UserCreateDTO userCreateDTO) {

        Long roleId = userCreateDTO.getRoleId();

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role Not Found"));

        User user = userMapper.toEntity(userCreateDTO, role);

        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

        userRepository.save(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findByIsDeletedFalse()
                .stream().map(userMapper::toResponseDTO).toList();
    }

    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        return userMapper.toResponseDTO(user);
    }

    public User updateUser(UserUpdateDTO userUpdateDTO, Long id) {


        User user1 = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        Role role = roleRepository.findById(userUpdateDTO.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException("Role Not Found"));


        user1.setName(userUpdateDTO.getName());
        user1.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        user1.setRole(role);
        return userRepository.save(user1);
    }

    public void softDelete(Long id) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        user.setIsDeleted(true);
        userRepository.save(user);
    }
}
