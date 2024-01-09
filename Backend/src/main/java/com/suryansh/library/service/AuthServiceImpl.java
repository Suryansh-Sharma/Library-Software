package com.suryansh.library.service;

import com.suryansh.library.dto.LoginResponse;
import com.suryansh.library.entity.UserEntity;
import com.suryansh.library.exception.SpringLibraryException;
import com.suryansh.library.model.UserLoginModel;
import com.suryansh.library.repository.UserEntityRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    private final UserEntityRepo userEntityRepo;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(UserEntityRepo userEntityRepo) {
        this.userEntityRepo = userEntityRepo;
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
                true
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
                    true
            );
        }catch (Exception e){
            logger.error("Unable to create user "+e);
            throw new SpringLibraryException(e.getMessage(),"SomethingWentWrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
