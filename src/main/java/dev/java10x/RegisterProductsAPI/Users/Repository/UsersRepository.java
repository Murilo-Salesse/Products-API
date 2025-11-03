package dev.java10x.RegisterProductsAPI.Users.Repository;

import dev.java10x.RegisterProductsAPI.Users.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UserModel, Integer> {
}
