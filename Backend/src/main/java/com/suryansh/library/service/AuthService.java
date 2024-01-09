package com.suryansh.library.service;

import com.suryansh.library.dto.LoginResponse;
import com.suryansh.library.model.UserLoginModel;

public interface AuthService {
    LoginResponse verifyUser(UserLoginModel model);

    LoginResponse createAccount(UserLoginModel model);
}
