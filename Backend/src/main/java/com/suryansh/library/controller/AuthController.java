package com.suryansh.library.controller;

import com.suryansh.library.dto.LoginResponse;
import com.suryansh.library.model.UserLoginModel;
import com.suryansh.library.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth/")
@CrossOrigin("*")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public LoginResponse login(@Valid @RequestBody UserLoginModel model){
        return authService.verifyUser(model);
    }

    @PostMapping("signUp")
    public LoginResponse signUp(@Valid @RequestBody UserLoginModel model){
        return authService.createAccount(model);
    }
    @GetMapping("new-jwt-by-refresh-token")
    public LoginResponse newJwtByRefreshToken(@RequestParam String refresh_token){
        return authService.getNewJwtFromRefToken(refresh_token);
    }

}
