package com.suryansh.library.security;

import com.suryansh.library.entity.UserEntity;
import com.suryansh.library.exception.SpringLibraryException;
import com.suryansh.library.repository.UserEntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserEntityRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws SpringLibraryException {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new SpringLibraryException("Unable to find user of username " + username,
                        "IssuerNotFount", HttpStatus.NOT_FOUND)
                );
        return new CustomUserDetails(user);
    }
}
