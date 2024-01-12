package com.suryansh.library.service;

import com.suryansh.library.dto.LoginResponse;
import com.suryansh.library.entity.UserEntity;
import com.suryansh.library.exception.SpringLibraryException;
import com.suryansh.library.model.UserLoginModel;
import com.suryansh.library.repository.UserEntityRepo;
import com.suryansh.library.security.JwtService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    private final JwtService jwtService;
    private final UserEntityRepo userEntityRepo;
    @Value("${jwt-exp-time-min}")
    private long JWT_EXP_MIN;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(UserEntityRepo userEntityRepo, JwtService jwtService) {
        this.userEntityRepo = userEntityRepo;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse verifyUser(UserLoginModel model) {
        UserEntity user = userEntityRepo.findByUsername(model.getUsername())
                .orElseThrow(()->
                        new SpringLibraryException("Can't find user of this name ","WrongUsername", HttpStatus.NOT_FOUND));

        if (!user.getPassword().equals(model.getPassword())){
            throw new SpringLibraryException("Wrong Password check again !!","WrongPassword", HttpStatus.NOT_FOUND);
        }
        return new LoginResponse(
                user.getUsername(),
                user.getRole(),
                true,
                new LoginResponse.JwtResponse(jwtService.GenerateToken(user.getUsername()), JWT_EXP_MIN + " minutes to expire token")
        );
    }

    @Override
    @Transactional
    public LoginResponse createAccount(UserLoginModel model) {
        var checkUser = userEntityRepo.findByUsername(model.getUsername());
        if (checkUser.isPresent()){
            throw new SpringLibraryException("Sorry this username is already present !!","UsernamePresent", HttpStatus.CONFLICT);
        }
        UserEntity user = UserEntity.builder()
                .username(model.getUsername())
                .password(model.getPassword())
                .role("USER")
                .build();
        try {
            userEntityRepo.save(user);
            logger.info("New User {} is created successfully ",model);
            return new LoginResponse(
                    user.getUsername(),
                    user.getRole(),
                    true,
                    new LoginResponse.JwtResponse(jwtService.GenerateToken(user.getUsername()), JWT_EXP_MIN + " minutes to expire token")
            );
        }catch (Exception e){
            logger.error("Unable to create user "+e);
            throw new SpringLibraryException(e.getMessage(),"SomethingWentWrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
