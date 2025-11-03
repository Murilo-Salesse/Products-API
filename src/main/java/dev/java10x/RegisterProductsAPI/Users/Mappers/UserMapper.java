package dev.java10x.RegisterProductsAPI.Users.Mappers;

import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTO;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTOResponse;
import dev.java10x.RegisterProductsAPI.Users.Models.UserModel;

public class UserMapper {

    // Metodo para mapear de UserDTO para UserModel
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

    // Metodo para mapear de UserModel para UserDTOResponse
    public UserDTOResponse toUserDTOResponse(UserModel userModel) {
        if (userModel == null) {
            return null;
        }

        return new UserDTOResponse(
                userModel.getId(),
                userModel.getEmail(),
                userModel.getName()
        );
    }
}
