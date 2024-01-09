package com.suryansh.library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_entity")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String role;
}
