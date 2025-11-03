package dev.java10x.RegisterProductsAPI.Users.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOResponse {

    private Long id;
    private String email;
    private String name;
}
