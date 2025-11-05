package dev.java10x.RegisterProductsAPI.Users.Mappers;

import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTO;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTOResponseWithToken;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTOResponseWithoutToken;
import dev.java10x.RegisterProductsAPI.Users.Models.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Converte de UserDTO (entrada) para UserModel (entidade do banco)
    public UserModel toUserModel(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        UserModel userModel = new UserModel();
        userModel.setName(userDTO.getName());
        userModel.setEmail(userDTO.getEmail());
        userModel.setPassword(userDTO.getPassword());

        return userModel;
    }


    public UserDTOResponseWithToken toUserDTOResponse(UserModel userModel) {
        if (userModel == null) {
            return null;
        }


        return new UserDTOResponseWithToken(
                userModel.getId(),
                userModel.getName(),
                userModel.getEmail(),
                null // token será definido no service após gerar o JWT
        );
    }

    public UserDTOResponseWithoutToken toUserDTOResponseNoToken(UserModel userModel) {
        if (userModel == null) {
            return null;
        }


        return new UserDTOResponseWithoutToken(
                userModel.getId(),
                userModel.getName(),
                userModel.getEmail()
        );
    }
}