package dev.java10x.RegisterProductsAPI.Users;

import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTO;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTOResponseWithToken;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTOResponseWithoutToken;
import dev.java10x.RegisterProductsAPI.Users.Mappers.UserMapper;
import dev.java10x.RegisterProductsAPI.Users.Models.UserModel;
import dev.java10x.RegisterProductsAPI.Users.Repository.UsersRepository;
import dev.java10x.RegisterProductsAPI.Users.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDTO user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new UserDTO();
        user.setId(1L);
        user.setEmail("teste@gmail.com");
        user.setName("Nome Teste");
        user.setPassword("123456");
    }

    @Test
    @DisplayName("Must create a user")
    void mustCreateUser() {
        // Arrange
        UserModel userModel = new UserModel();
        userModel.setEmail(user.getEmail());
        userModel.setName(user.getName());
        userModel.setPassword("encoded123");

        when(userMapper.toUserModel(any(UserDTO.class))).thenReturn(userModel);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded123");
        when(usersRepository.save(any(UserModel.class))).thenAnswer(inv -> {
            UserModel saved = inv.getArgument(0);
            saved.setId(100L);
            return saved;
        });
        when(userMapper.toUserDTOResponseNoToken(any(UserModel.class))).thenReturn(
                new UserDTOResponseWithoutToken(100L, user.getEmail(), user.getName())
        );

        // Act
        UserDTOResponseWithoutToken result = userService.createUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("teste@gmail.com", result.getEmail());
        assertEquals("Nome Teste", result.getName());

        verify(usersRepository, times(1)).save(any(UserModel.class));
        verify(passwordEncoder, times(1)).encode(anyString());
    }
}