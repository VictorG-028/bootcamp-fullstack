package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.domain.entity.User;
import br.com.sysmap.bootcamp.domain.entity.Wallet;
import br.com.sysmap.bootcamp.domain.repository.UserRepository;
import br.com.sysmap.bootcamp.domain.util.Util;
import br.com.sysmap.bootcamp.dto.AuthDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
//    private final WalletService walletService; // Known problem: uncomment creates circular reference
    private final PasswordEncoder passwordEncoder;
    private final Util util;


    // =-= Endpoints section =-= //
    public User update(User newUser) {

        User oldUser = util.getLoggedUser();

        String newEmail = newUser.getEmail();
        String newName = newUser.getName();
        String newPassword = newUser.getPassword();

        // Assumes frontend sends a newUser with at least one property changed
        // Assumes frontend sends invalid properties or valid equal properties when they do not change
        // Change oldUser with newUser data
        if (this.validateEmail(newEmail)) {
            oldUser = oldUser.toBuilder().email(newEmail).build();
        }
        if (this.validateName(newName)) {
            oldUser = oldUser.toBuilder().name(newName).build();
        }
        if (this.validatePassword(newPassword)) {
            String encoded = this.passwordEncoder.encode(newPassword);
            oldUser = oldUser.toBuilder().password(encoded).build();
        }

        return this.userRepository.save(oldUser); // Resave with new data
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public User create(User user, WalletService walletService) throws InvalidAttributeValueException, EntityExistsException {

        String email = user.getEmail();
        String name = user.getName();
        String password = user.getPassword();


        boolean notValidEmail = !this.validateEmail(email);
        if (notValidEmail) {
            throw new InvalidAttributeValueException("Invalid email");
        }
        boolean notValidName = !this.validateName(name);
        if (notValidName) {
            throw new InvalidAttributeValueException("Invalid name");
        }
        boolean notValidPassword = !this.validatePassword(password);
        if (notValidPassword) {
            throw new InvalidAttributeValueException("Invalid password");
        };
        boolean userAlreadyExists = this.findByEmail(email) != null;
        if (userAlreadyExists) {
            throw new EntityExistsException("Email already exists");
        }

        String encoded = this.passwordEncoder.encode(password);

        user = user.toBuilder()
                   .password(encoded)
                   .build();

        Wallet wallet = walletService.createUserWallet(user);
        log.info("Created user ({}) with default wallet ({})", user, wallet);

        return this.userRepository.save(user);
    }


    public AuthDto auth(AuthDto authDto) throws UsernameNotFoundException, BadCredentialsException {
        String email = authDto.getEmail();
        String password = authDto.getPassword();

        Optional<User> existingUser = this.userRepository.findByEmail(email);

        if (existingUser.isEmpty()) {
            throw new UsernameNotFoundException("User " +email+ " doens't exists");
        }

        User user = existingUser.get();
        String encodedPass = user.getPassword();

        boolean isCorrectPass = this.passwordEncoder.matches(password, encodedPass);
        if (!isCorrectPass) {
            throw new BadCredentialsException("Incorrect password");
        }

        String emailWithPass = email + ":" + encodedPass;
        String base64token = Base64.getEncoder()
                                   .withoutPadding()
                                   .encodeToString(emailWithPass.getBytes());
        return AuthDto.builder()
                      .id(user.getId())
                      .email(email)
                      .token(base64token)
                      .build();
    }


    public List<User> getAllUsers() {

        return userRepository.findAll();
    }


    public User getUserById(Long userId) throws EntityNotFoundException {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new EntityNotFoundException("User with id " +userId+ " not found");
        }

        return user.get();
    }


    // =-= Util section =-= //
    /**
     * Loads the user details by the given email.
     *
     * @param email The email of the user to load.
     * @return The UserDetails object representing the user.
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = this.findByEmail(email);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<GrantedAuthority>() // Lista vazia de autoridade
        );
    }


    /**
     * Finds a user by email.
     *
     * @param email The email of the user to find.
     * @return The user with the given email, or null if not found.
     */
    public User findByEmail(String email) {
        return this.userRepository
                   .findByEmail(email)
                   .orElse(null);
    }


    /**
     * Returns true if the email format is valid.
     *
     * @param email The email to be validated.
     * @return true if the email is valid, false otherwise.
     */
    private boolean validateEmail(String email) {
        String isValidRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        boolean result = true;

        result = !email.matches(isValidRegex);
        result = !email.isBlank();

        return result;
    }


    /**
     * Returns true if the password format is valid.
     *
     * @param password The password to be validated.
     * @return true if the password meets the criteria, false otherwise.
     */
    private boolean validatePassword(String password) {

        boolean result = true;

        result = password.length() > 8;
        result = !password.isBlank();

        return result;
    }


    /**
     * Returns true if the name format is valid.
     *
     * @param name The name to be validated.
     * @return true if the name meets the criteria, false otherwise.
     */
    private boolean validateName(String name) {

        boolean result = true;

        result = !name.isBlank();

        return result;
    }
}

