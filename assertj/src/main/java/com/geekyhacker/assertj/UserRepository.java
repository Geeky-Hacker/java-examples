package com.geekyhacker.assertj;

public class UserRepository {

    public User findByUsername(String username) throws UserNotFoundException {
        if (!"test".equalsIgnoreCase(username)) {
            throw new UserNotFoundException("Cannot find username: %s".formatted(username));
        }
        return new User("test", "first name", "last name");
    }
}
