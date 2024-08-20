package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.domain.entity.User;
import br.com.sysmap.bootcamp.domain.service.UserService;
import br.com.sysmap.bootcamp.domain.service.WalletService;
import br.com.sysmap.bootcamp.dto.AuthDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


import javax.management.InvalidAttributeValueException;
import java.util.List;


@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final WalletService walletService;


    @Operation(summary = "Update user")
    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {

        try {
            User newUser = this.userService.update(user);
            return ResponseEntity.ok(newUser);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    @Operation(summary = "Save user")
    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody User user) {

        try {
            // Pass walletService to avoid circular reference
            User newUser = this.userService.create(user, this.walletService);

            return ResponseEntity.ok(newUser);
        } catch (InvalidAttributeValueException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


    @Operation(summary = "Auth user")
    @PostMapping("/auth")
    public ResponseEntity<AuthDto> auth(@RequestBody AuthDto user) {

        try {
            return ResponseEntity.ok(this.userService.auth(user));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @Operation(summary = "List all users")
    @GetMapping("/")
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> userList = this.userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }


    @Operation(summary = "Get user by ID")
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {

        try {
            User user = this.userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
