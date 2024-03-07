package com.monitoring.application.user.service;

import com.monitoring.application.configuration.jwt.JWTException;
import com.monitoring.application.configuration.jwt.JWTProvider;
import com.monitoring.application.user.model.DTO.SignInDTO;
import com.monitoring.application.user.model.DTO.SignUpDTO;
import com.monitoring.application.user.model.User;
import com.monitoring.application.user.repository.UserRepository;
import com.monitoring.application.user.service.exception.UserExistsException;
import com.monitoring.application.user.service.exception.UserNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;

    public UserServiceImpl(UserRepository userRepository, JWTProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User userEntity = findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return userEntity;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by("id").descending());
    }

    @Override
    @Transactional(readOnly = true)
    public User findUser(Long id) {
        final Optional<User> user = userRepository.findById(id);

        return user.orElseThrow(() -> new UserNotFoundException(id));
    }
    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }
    @Override
    @Transactional
    public void createUser(SignUpDTO userInfo) {
        if (userRepository.findOneByUsername(userInfo.getUsername()) != null) {
            throw new UserExistsException(userInfo.getUsername());
        }
        final User user = new User(userInfo.getUsername(), userInfo.getPassword(), userInfo.getRole());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public String loginUser(SignInDTO userInfo) {
        final User user = findByUsername(userInfo.getUsername());
        if (user == null) {
            throw new UserNotFoundException(userInfo.getUsername());
        }
        if (!user.getPassword().equals(userInfo.getPassword())) {
            throw new UserNotFoundException(user.getUsername());
        }
        return jwtProvider.generateToken(user.getUsername());
    }

    @Override
    @Transactional
    public Boolean deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public UserDetails loadUserByToken(String token) throws UsernameNotFoundException {
        if (!jwtProvider.isTokenValid(token)) {
            throw new JWTException("Bad token");
        }
        final String userLogin = jwtProvider.getLoginFromToken(token)
                .orElseThrow(() -> new JWTException("Token is not contain Login"));
        return loadUserByUsername(userLogin);
    }
}
