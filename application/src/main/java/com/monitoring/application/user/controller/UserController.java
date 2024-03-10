package com.monitoring.application.user.controller;

import com.monitoring.application.configuration.OpenApi30Configuration;
import com.monitoring.application.user.model.DTO.SignInDTO;
import com.monitoring.application.user.model.DTO.SignUpDTO;
import com.monitoring.application.user.model.DTO.ViewUserDTO;
import com.monitoring.application.user.model.User;
import com.monitoring.application.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Tag(name = "Пользователи", description = "API для пользователей")
@RestController
@RequestMapping(OpenApi30Configuration.API_PREFIX + "/users")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(
            summary = "Войти в систему")
    @PostMapping(value = "/signIn")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid SignInDTO userLoginDto) {
        String token = userService.loginUser(userLoginDto);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(
            summary = "Зарегистрировать нового пользователя (только админ)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/signUp")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpDTO userSignupDto){
        userService.createUser(userSignupDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Operation(
            summary = "Удалить пользователя (только админ)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") long id){
        final boolean deleted = userService.deleteUser(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    @Operation(
            summary = "Получить всех пользователей (только админ)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "")
    public ResponseEntity<List<ViewUserDTO>> readAll() {
        final List<User> rules = userService.getAllUsers();
        return rules != null
                ? new ResponseEntity<>(rules.stream().map(ViewUserDTO::new).toList(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
