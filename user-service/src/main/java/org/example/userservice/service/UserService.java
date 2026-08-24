package org.example.userservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.userservice.dto.CreateUserDto;
import org.example.userservice.dto.PageResponseDto;
import org.example.userservice.dto.UpdateUserDto;
import org.example.userservice.dto.UserDto;
import org.example.userservice.entities.Role;
import org.example.userservice.entities.User;
import org.example.userservice.exception.UserNotFoundException;
import org.example.userservice.repository.UserRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Data
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository ;
    private final CacheManager cacheManager ;

    private User getLoggedInUser() {
        String email = Objects.requireNonNull(Objects
                        .requireNonNull(SecurityContextHolder.getContext())
                        .getAuthentication())
                .getName() ;
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found!")) ;
    }

    public UserDto getCurrentUser() {
        User user = getLoggedInUser() ;
        return map(user) ;
    }

    @Transactional
    public UserDto updateCurrentUser(UpdateUserDto dto) {
        User user = getLoggedInUser();

        String oldEmail = user.getEmail();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        UserDto updated = map(user);

        // invalidate/update email cache if email changed
        if (!oldEmail.equals(user.getEmail())) {
            Cache cache = cacheManager.getCache("user-service-usersByEmail");

            if (cache != null) {
                cache.evict(oldEmail);
                cache.put(user.getEmail(), updated);
            }
        }

        return updated;
    }

    @Transactional
    public String deleteCurrentUser() {

        User currentUser = getLoggedInUser();

        Long id = currentUser.getId();
        String email = currentUser.getEmail();

        userRepository.delete(currentUser);

        Cache userCache = cacheManager.getCache("user-service-users");
        if (userCache != null) {
            userCache.evict(id);
        }

        Cache emailCache = cacheManager.getCache("user-service-usersByEmail");
        if (emailCache != null) {
            emailCache.evict(email);
        }

        return "Successfully deleted your account";
    }

    public UserDto saveUser(CreateUserDto createUserDto) {
        User user = new User() ;
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        User savedUser = userRepository.save(user) ;
        return new UserDto(savedUser.getId(), savedUser.getName(), savedUser.getEmail()) ;
    }

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll() ;
        List<UserDto> userDtos = new ArrayList<>() ;
        for(User user : users) {
            UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail()) ;
            userDtos.add(userDto) ;
        }
        return userDtos ;
    }

    @Cacheable(value = "user-service-users", key = "#id")
    public UserDto getUserById(Long id) {
        User user =  userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found with ID : " + id));
        return new UserDto(user.getId(), user.getName(), user.getEmail()) ;
    }

    public List<UserDto> getUserByName(String name) {
        List<User> users = userRepository.findByName(name) ; ;
        List<UserDto> userDtos = new ArrayList<>() ;
        for(User user : users) {
            UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail()) ;
            userDtos.add(userDto) ;
        }
        return userDtos ;
    }

    @Cacheable(value = "user-service-usersByEmail", key = "#email")
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
        return map(user);
    }

    @Transactional
    @CacheEvict(value = "user-service-users", key = "#id")
    public String deleteUserById(Long id) {

        User currentUser = getLoggedInUser();

        User targetUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with ID : " + id));

        // Admin cannot delete another admin
        if (targetUser.getRole() == Role.ADMIN && !targetUser.getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(
                    "Admin cannot delete other admin");
        }

        userRepository.delete(targetUser);

        return "Successfully deleted user with ID : " + id;
    }

    @Transactional
    @CacheEvict(value = "user-service-users", key = "#id")
    public UserDto updateUserById(Long id, UpdateUserDto updateUserDto) {

        User currentUser = getLoggedInUser();

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User Not Found with ID : " + id));

        // ADMIN cannot update another ADMIN
        if (user.getRole() == Role.ADMIN
                && !currentUser.getId().equals(user.getId())) {

            throw new AccessDeniedException(
                    "Admin cannot update another admin");
        }

        // Store old email before changing it
        String oldEmail = user.getEmail();

        // Update entity
        user.setName(updateUserDto.getName());
        user.setEmail(updateUserDto.getEmail());

        UserDto updatedUserDto = map(user);

        // Update email-based cache
        Cache cache = cacheManager.getCache("user-service-usersByEmail");

        if (cache != null) {

            // Remove old email → old user data
            cache.evict(oldEmail);

            // Store new email → updated user data
            cache.put(user.getEmail(), updatedUserDto);
        }

        return updatedUserDto;
    }

    @Transactional
    @CacheEvict(value = "user-service-users", key = "#id")
    public UserDto updateUserByIdPartial(
            Long id,
            Map<String, Object> updates) {

        User currentUser = getLoggedInUser();

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User Not Found with ID : " + id));

        // ADMIN cannot update another ADMIN
        if (user.getRole() == Role.ADMIN
                && !currentUser.getId().equals(user.getId())) {

            throw new AccessDeniedException(
                    "Admin cannot update another admin");
        }

        // Store old email
        String oldEmail = user.getEmail();

        // Apply partial updates
        if (updates.containsKey("name")) {
            user.setName((String) updates.get("name"));
        }

        if (updates.containsKey("email")) {
            user.setEmail((String) updates.get("email"));
        }

        UserDto updatedUserDto = map(user);

        // Update email cache
        Cache cache = cacheManager.getCache("user-service-usersByEmail");

        if (cache != null) {

            // Remove old email cache
            cache.evict(oldEmail);

            // Add new email cache
            cache.put(user.getEmail(), updatedUserDto);
        }

        return updatedUserDto;
    }

    public List<UserDto> getUsersPaginated(int page, int pageSize, String direction, String sortBy) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending() ;
        Pageable pageable = PageRequest.of(page, pageSize, sort) ;
        Page<User> usersPage = userRepository.findAll(pageable) ;
        List<UserDto> userDtos = new ArrayList<>() ;
        usersPage.forEach(user -> userDtos.add(new UserDto(user.getId(), user.getName(), user.getEmail())) );
//        for(User user : usersPage) {
//            UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail()) ;
//            userDtos.add(userDto) ;
//        }
        return userDtos ;
    }

    public PageResponseDto<UserDto> searchUsers(String name, Pageable pageable) {
        Page<UserDto> page = userRepository
                .findByNameContainingIgnoreCase(name, pageable)
                .map(user ->
                        new UserDto(
                                user.getId(),
                                user.getName(),
                                user.getEmail()
                        ));
        return toPageResponse(page) ;
    }

    public UserDto map(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail()) ;
    }

    private <T> PageResponseDto<T> toPageResponse(Page<T> page) {

        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
