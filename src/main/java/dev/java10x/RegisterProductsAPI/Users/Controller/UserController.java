package dev.java10x.RegisterProductsAPI.Users.Controller;

import dev.java10x.RegisterProductsAPI.Security.JwtTokenUtil;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTO;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTOLogin;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTOResponseWithToken;
import dev.java10x.RegisterProductsAPI.Users.DTOS.UserDTOResponseWithoutToken;
import dev.java10x.RegisterProductsAPI.Users.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTOResponseWithoutToken> createUser(@Valid @RequestBody UserDTO dto) {
        UserDTOResponseWithoutToken createduser = userService.createUser(dto);
        return ResponseEntity.status(201).body(createduser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTOResponseWithToken> loginUser(@Valid @RequestBody UserDTOLogin dto) {
        UserDTOResponseWithToken loggedUser = userService.verifyIfUserExists(dto);
        return ResponseEntity.status(200).body(loggedUser);
    }
}
