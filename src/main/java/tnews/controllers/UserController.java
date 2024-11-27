package tnews.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tnews.dto.UserCreateDTO;
import tnews.dto.UserResponseDTO;
import tnews.mapper.UserMapper;
import tnews.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.findAll().stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return UserMapper.toDTO(userService.findById(id));
    }

    @PostMapping
    public UserResponseDTO createUser (@RequestBody UserCreateDTO userCreateDTO) {
        return UserMapper.toDTO(userService.create(UserMapper.fromDTO(userCreateDTO)));
    }

    @PostMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody UserCreateDTO userCreateDTO) {
        return UserMapper.toDTO(userService.update(id, UserMapper.fromDTO(userCreateDTO)));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

}
