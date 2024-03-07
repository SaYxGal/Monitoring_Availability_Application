package com.monitoring.application.user.service;

import com.monitoring.application.user.model.DTO.SignInDTO;
import com.monitoring.application.user.model.DTO.SignUpDTO;
import com.monitoring.application.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    User findUser(Long id);
    User findByUsername(String username);
    void createUser(SignUpDTO userInfo);
    String loginUser(SignInDTO userInfo);
    Boolean deleteUser(Long id);
    UserDetails loadUserByToken(String token) throws UsernameNotFoundException;
}
