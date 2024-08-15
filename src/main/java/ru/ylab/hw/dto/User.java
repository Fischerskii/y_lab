package ru.ylab.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ylab.hw.enums.Role;

@Data
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private Role role;
}