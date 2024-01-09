package com.suryansh.library.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class UserLoginModel {
    @NotNull(message = "username can't be null")
    @NotBlank(message = "username can't be blank")
    private String username;
    @NotNull(message = "password can't be null")
    @NotBlank(message = "username can't be blank")
    private String password;
}
