package org.example.userservice.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.userservice.dto.CreateUserDto;
import org.example.userservice.dto.PageResponseDto;
import org.example.userservice.dto.UpdateUserDto;
import org.example.userservice.dto.UserDto;
import org.example.userservice.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService ;

    // USER -> own profile
    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe() {
        return ResponseEntity.ok(userService.getCurrentUser()) ;
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMe(@RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(userService.updateCurrentUser(updateUserDto)) ;
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMe() {
        return ResponseEntity.ok(userService.deleteCurrentUser()) ;
    }

    // ADMIN -> All Users
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers()) ;
    }

    // SECURE THIS.
    // ADMIN -> All Users
    @GetMapping("/paginated")
    public ResponseEntity<List<UserDto>> getUsersPaginated(@RequestParam int page, @RequestParam int pageSize,
                                                           @RequestParam(defaultValue = "asc") String direction,
                                                           @RequestParam(defaultValue = "name") String sortBy) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsersPaginated(page, pageSize, direction, sortBy)) ;
    }

    // ADMIN -> Specific User
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id)) ;
    }

    // ADMIN -> Specific Users
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByEmail(email)) ;
    }

    // ADMIN -> Search Users
    @GetMapping("/name/search")
    public ResponseEntity<PageResponseDto<UserDto>> searchUsers(@RequestParam String name,
                                                                Pageable pageable) {
        return ResponseEntity.ok(
                userService.searchUsers(name, pageable)
        );
    }

    // ADMIN -> Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUserById(id)) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable Long id,
                                           @Valid @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserById(id, updateUserDto)) ;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUserByIdPartial(@PathVariable Long id,
                                                      @RequestBody Map<String, Object> updates) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserByIdPartial(id, updates)) ;
    }

}
