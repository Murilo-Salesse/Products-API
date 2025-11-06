package dev.java10x.RegisterProductsAPI.Users.Repository;

import dev.java10x.RegisterProductsAPI.Users.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UserModel, Integer> {

    Optional<UserModel> findByEmail(String email);
}
