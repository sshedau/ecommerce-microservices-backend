package org.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserDto {
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String name ;
    @Email
    @NotNull
    @NotBlank
    private String email ;
    @NotBlank
    @NotNull
    private String password ;
}
