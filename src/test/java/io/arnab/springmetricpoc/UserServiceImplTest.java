package io.arnab.springmetricpoc;

import io.arnab.springmetricpoc.application.UserService;
import io.arnab.springmetricpoc.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(BaseTestConfig.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testAddUser() {
        var user = userService.addUser("John Doe", "john.doe@example.com");
        assertThat(user).isNotNull();
        assertThat(user.id()).isNotEmpty();

        var response = userService.getUser(user.id());
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.email()).isEqualTo("john.doe@example.com");
    }
}
