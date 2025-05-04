package se.dmolinsky.webshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import se.dmolinsky.webshop.model.User;
import se.dmolinsky.webshop.repository.UserRepository;
import se.dmolinsky.webshop.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final String plainPassword = "secret123";
    private final String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword(hashedPassword);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        Optional<User> userOpt = userService.getByUsername("testuser");
        assertTrue(userOpt.isPresent());

        boolean passwordMatches = userService.checkPassword(plainPassword, userOpt.get().getPassword());
        assertTrue(passwordMatches, "Plan password should match the hashed version");
    }

}
