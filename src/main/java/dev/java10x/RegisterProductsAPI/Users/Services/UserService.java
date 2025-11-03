package dev.java10x.RegisterProductsAPI.Users.Services;

import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTO;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTOResponse;
import dev.java10x.RegisterProductsAPI.Users.Mappers.UserMapper;
import dev.java10x.RegisterProductsAPI.Users.Models.UserModel;
import dev.java10x.RegisterProductsAPI.Users.Repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    public UserService(UsersRepository usersRepository, UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
    }

    public UserDTOResponse createUser(UserDTO dto) {
        UserModel user = userMapper.toUserModel(dto);
        UserModel savedUser = usersRepository.save(user);

        return userMapper.toUserDTOResponse(savedUser);
    }
}
