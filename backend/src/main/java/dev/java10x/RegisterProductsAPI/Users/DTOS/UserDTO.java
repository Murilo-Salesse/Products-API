package dev.java10x.RegisterProductsAPI.Users.DTOS;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "Email do usuário não pode ser vazio.")
    private String email;

    @NotBlank(message = "Nome do usuário não pode ser vazio.")
    private String name;

    @NotNull(message = "Senha não pode ser vazia.")
    private String password;
}
