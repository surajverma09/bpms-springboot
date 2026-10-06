package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.Role;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.UserNotFoundException;
import com.suraj.bpms.project.repository.RoleRepository;
import com.suraj.bpms.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User createUser(User user) {
        Long roleId = user.getRole().getId();

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role Not Found"));
        user.setRole(role);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findByIsDeletedFalse();
    }

    public User getUserById(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    }

    public User updateUser(User user, Long id) {


        User user1 = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        user1.setName(user.getName());
        user1.setPassword(user.getPassword());

        return userRepository.save(user1);
    }

    public void softDelete(Long id) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        user.setIsDeleted(true);
        userRepository.save(user);
    }
}
