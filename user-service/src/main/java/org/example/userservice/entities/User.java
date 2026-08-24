package org.example.userservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name ;
    @Column(unique = true, nullable = false)
    private String email ;
    @Column(nullable = false)
    private String password ;
    @Enumerated(EnumType.STRING)
    private Role role ;
}
