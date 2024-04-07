package com.geekyhacker.assertj;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @InjectMocks
    private UserRepository userRepository;

    @Test
    void shouldReturnUserByUsername() throws UserNotFoundException {
        var expected = new User("test", "first name", "last name");

        var result = userRepository.findByUsername("test");

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldThrowUserNotFoundException() {
        assertThatThrownBy(() -> userRepository.findByUsername("nonExistenceUsername"))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("Cannot find username: nonExistenceUsername");
    }

    @Test
    void shouldVerifyUserNotFoundExceptionIsThrown() {
        assertThrows(UserNotFoundException.class, () -> userRepository.findByUsername("nonExistenceUsername"));
    }
}
