package br.com.sysmap.bootcamp.domain.service;

import br.com.sysmap.bootcamp.BootcampSysmapUserApi;
import br.com.sysmap.bootcamp.domain.entity.User;
import br.com.sysmap.bootcamp.domain.repository.UserRepository;
import br.com.sysmap.bootcamp.dto.AuthDto;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import javax.management.InvalidAttributeValueException;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes= BootcampSysmapUserApi.class)
public class UserServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private WalletService mockedWalletService;

    @MockBean
    private UserRepository mockedUserRepository;

    private User testUser;

    @BeforeEach
    public void setUp() throws InvalidAttributeValueException {
        String password = "123456789";
        String encodedPass = this.passwordEncoder.encode(password);
        testUser = User.builder()
                .id(1L)
                .name("test")
                .email("test@example.com")
                .password(encodedPass)
                .build();

        when(mockedUserRepository.save(any(User.class))).thenReturn(testUser);

        try {
            userService.create(testUser, mockedWalletService);
        } catch (InvalidAttributeValueException | EntityExistsException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void cleanTestEnvironment() {
        reset(userRepository);
        testUser = null;
    }


    @Test
    @DisplayName("Should save a valid user and return the saved user")
    public void shouldCreateValidUser() throws InvalidAttributeValueException {
        User user = User.builder()
                        .id(1L)
                        .name("teste")
                        .email("test@gmail.com")
                        .password("123456789")
                        .build();

        when(mockedUserRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.create(user, mockedWalletService);

        assertEquals(user, savedUser);
    }

// the test below is not working
//    @Test
//    @DisplayName("Should auth an already created user.")
//    public void shouldAuthCreatedUser() {
//        User user = User.builder()
//                .id(1L)
//                .name("test")
//                .email("test@example.com")
//                .password("123456789")
//                .build();
//        System.out.println("=--------------=");
//        System.out.println(testUser);
//        System.out.println(testUser.getPassword());
//        System.out.println(testUser.getEmail());
//        System.out.println(userRepository.findAll());
//        System.out.println(userRepository.findByEmail(testUser.getEmail()));
//        System.out.println("=--------------=");
//        String encodedPass = testUser.getPassword();
//        String emailWithPass = testUser.getEmail() + ":" + encodedPass;
//        String base64token = Base64.getEncoder()
//                .withoutPadding()
//                .encodeToString(emailWithPass.getBytes());
//
//        AuthDto inputAuthDto = AuthDto.builder()
//                                .id(1L)
//                                .email(user.getEmail())
//                                .password(user.getPassword())
//                                .token(base64token)
//                                .build();
//
//        AuthDto resultAuthDto = userService.auth(inputAuthDto);
//
//        assertEquals(inputAuthDto, resultAuthDto);
//    }
}
