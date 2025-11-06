package dev.java10x.RegisterProductsAPI.Users.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "senha")
    private String password;
}
