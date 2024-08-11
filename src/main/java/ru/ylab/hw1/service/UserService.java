package ru.ylab.hw1.service;

import ru.ylab.hw1.dto.UserDTO;
import ru.ylab.hw1.enums.Role;

import java.util.List;

public interface UserService {

    void register(String username, String password, Role role);

    UserDTO login(String username, String password);

    List<UserDTO> viewUsers();
}
