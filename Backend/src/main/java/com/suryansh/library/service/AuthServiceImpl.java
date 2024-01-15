package com.suryansh.library.service;

import com.suryansh.library.dto.LoginResponse;
import com.suryansh.library.entity.RefreshTokenEntity;
import com.suryansh.library.entity.UserEntity;
import com.suryansh.library.exception.SpringLibraryException;
import com.suryansh.library.model.UserLoginModel;
import com.suryansh.library.repository.RefreshTokenRepository;
import com.suryansh.library.repository.UserEntityRepo;
import com.suryansh.library.security.JwtService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService{
    private final JwtService jwtService;
    private final UserEntityRepo userEntityRepo;
    private final RefreshTokenRepository refreshTokenRepo;
    @Value("${jwt-exp-time-min}")
    private long JWT_EXP_MIN;
    @Value("${refresh-token-day}")
    private int REFRESH_TOKEN_DAY;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(UserEntityRepo userEntityRepo, JwtService jwtService, RefreshTokenRepository refreshTokenRepo) {
        this.userEntityRepo = userEntityRepo;
        this.jwtService = jwtService;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    @Override
    public LoginResponse verifyUser(UserLoginModel model) {
        UserEntity user = userEntityRepo.findByUsername(model.getUsername())
                .orElseThrow(()->
                        new SpringLibraryException("Can't find user of this name ","WrongUsername", HttpStatus.NOT_FOUND));

        if (!user.getPassword().equals(model.getPassword())){
            throw new SpringLibraryException("Wrong Password check again !!","WrongPassword", HttpStatus.NOT_FOUND);
        }
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDate.now().plusDays(REFRESH_TOKEN_DAY))
                .user(user)
                .build();
        try{
            refreshTokenRepo.save(refreshToken);
            logger.info("New Refresh Token is saved for user {} ",user.getUsername());
        }catch (Exception e){
            logger.error("Unable to save refresh token "+e);
            throw new SpringLibraryException("Something gone wrong please contact developer",
                    "SomethingWentWrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new LoginResponse(
                user.getUsername(),
                user.getRole(),
                true,
                new LoginResponse.JwtResponse(jwtService.GenerateToken(user.getUsername())
                        , JWT_EXP_MIN + " minutes to expire token",refreshToken.getToken())
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
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDate.now().plusDays(REFRESH_TOKEN_DAY))
                .user(user)
                .build();
        try {
            refreshTokenRepo.save(refreshToken);
            userEntityRepo.save(user);
            logger.info("New User {} is created successfully ",model);
            return new LoginResponse(
                    user.getUsername(),
                    user.getRole(),
                    true,
                    new LoginResponse.JwtResponse(jwtService.GenerateToken(user.getUsername()),
                            JWT_EXP_MIN + " minutes to expire token",refreshToken.getToken())
            );
        }catch (Exception e){
            logger.error("Unable to create user "+e);
            throw new SpringLibraryException(e.getMessage(),"SomethingWentWrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public LoginResponse getNewJwtFromRefToken(String token) {
        RefreshTokenEntity refreshToken = refreshTokenRepo.findByToken(token)
                .orElseThrow(()->new SpringLibraryException("Refresh Token Not Found !!",
                        "RefreshTokenNotFound",HttpStatus.NOT_FOUND));
        if (refreshToken.getExpiryDate().isBefore(LocalDate.now())){
            try{
                refreshTokenRepo.deleteRefreshToken(refreshToken.getId());
                logger.info("Expired refresh token is deleted !!");
                throw new SpringLibraryException("Sorry your exception is expired, please re login again",
                        "SessionExpired",HttpStatus.UNAUTHORIZED);
            }catch (Exception e){
                logger.error("Unable to delete refresh token "+e);
                throw e;
            }
        }else{
            UserEntity user = refreshToken.getUser();
            logger.info("New Jwt is created for user {} ",user.getUsername());
            return new LoginResponse(
                    user.getUsername(),
                    user.getRole(),
                    true,
                    new LoginResponse.JwtResponse(jwtService.GenerateToken(user.getUsername()),
                            JWT_EXP_MIN + " minutes to expire token",refreshToken.getToken())
            );
        }
    }
}
