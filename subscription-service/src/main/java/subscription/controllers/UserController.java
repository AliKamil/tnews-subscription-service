package subscription.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import subscription.dto.UserCreateDTO;
import subscription.dto.UserResponseDTO;
import subscription.mapper.UserMapper;
import subscription.service.UserService;

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
    public UserResponseDTO getUserById(@PathVariable("id") Long id) {
        return UserMapper.toDTO(userService.findById(id));
    }

    @PostMapping
    public UserResponseDTO createUser (@RequestBody @Valid UserCreateDTO userCreateDTO) {
        return UserMapper.toDTO(userService.create(UserMapper.fromDTO(userCreateDTO)));
    }

    @PostMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserCreateDTO userCreateDTO) {
        return UserMapper.toDTO(userService.update(id, UserMapper.fromDTO(userCreateDTO)));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
    }

}
