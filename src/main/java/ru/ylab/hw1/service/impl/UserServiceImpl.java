package ru.ylab.hw1.service.impl;

import ru.ylab.hw1.repository.UserRepository;
import ru.ylab.hw1.service.UserService;
import ru.ylab.hw1.dto.User;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(String username, String password, User.Role role) {
        userRepository.save(new User(username, password, role));
    }

    public User login(String username, String password) {
        User user = userRepository.findAll().get(username);
        return (user != null && user.getPassword().equals(password)) ? user : null;
    }

    public void viewUsers() {
        userRepository.findAll().values().forEach(System.out::println);
    }
}
