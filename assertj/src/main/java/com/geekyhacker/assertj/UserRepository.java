package com.geekyhacker.assertj;

import java.util.Optional;

public class UserRepository {

    public Optional<User> findBy(String username) {
        try {
            return Optional.of(findByUsername(username));
        } catch (UserNotFoundException userNotFoundException) {
            return Optional.empty();
        }
    }

    public User findByUsername(String username) throws UserNotFoundException {
        if (!"test".equalsIgnoreCase(username)) {
            throw new UserNotFoundException("Cannot find username: %s".formatted(username));
        }
        return new User("test", "first name", "last name");
    }
}
