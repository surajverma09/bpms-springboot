package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.dto.ApiResponse;
import com.suraj.bpms.project.dto.user.UserCreateDTO;
import com.suraj.bpms.project.dto.user.UserResponseDTO;
import com.suraj.bpms.project.dto.user.UserUpdateDTO;
import com.suraj.bpms.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
         userService.createUser(userCreateDTO);
         return new ResponseEntity<>(new ApiResponse("User created successfully"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        userService.updateUser(userUpdateDTO, id);
        return new  ResponseEntity<>(new ApiResponse("User updated succcessfully"), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.softDelete(id);
        return new ResponseEntity<>(new ApiResponse("User deleted successfully"), HttpStatus.OK
        );
    }
}