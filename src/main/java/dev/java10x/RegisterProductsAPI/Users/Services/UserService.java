package dev.java10x.RegisterProductsAPI.Users.Services;

import dev.java10x.RegisterProductsAPI.Security.JwtTokenUtil;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTO;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTOLogin;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTOResponseWithToken;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTOResponseWithoutToken;
import dev.java10x.RegisterProductsAPI.Users.Mappers.UserMapper;
import dev.java10x.RegisterProductsAPI.Users.Models.UserModel;
import dev.java10x.RegisterProductsAPI.Users.Repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public UserService(UsersRepository usersRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public UserDTOResponseWithoutToken createUser(UserDTO dto) {
        String rawPassword = dto.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        dto.setPassword(encodedPassword);

        UserModel savedUser = usersRepository.save(userMapper.toUserModel(dto));

        return userMapper.toUserDTOResponseNoToken(savedUser);
    }

    public UserDTOResponseWithToken verifyIfUserExists(UserDTOLogin dto) {
        UserModel user = usersRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        boolean passwordMatches = passwordEncoder.matches(dto.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new EntityNotFoundException("Senha incorreta");
        }

        String token = jwtTokenUtil.generateToken(user.getName(), user.getEmail());

        UserDTOResponseWithToken response = userMapper.toUserDTOResponse(user);
        response.setToken(token);

        return response;
    }
}
