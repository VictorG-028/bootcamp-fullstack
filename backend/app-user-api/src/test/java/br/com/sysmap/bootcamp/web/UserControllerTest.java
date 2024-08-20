package br.com.sysmap.bootcamp.web;

import br.com.sysmap.bootcamp.BootcampSysmapUserApi;
import br.com.sysmap.bootcamp.domain.entity.User;
import br.com.sysmap.bootcamp.domain.service.UserService;
import br.com.sysmap.bootcamp.domain.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes= BootcampSysmapUserApi.class)
public class UserControllerTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @MockBean
    private WalletService mockedWalletService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should save a valid user and return the saved user")
    public void shouldCreateValidUser() throws Exception {
        String password = "123456789";
        String encodedPass = this.passwordEncoder.encode(password);
        User user = User.builder()
                        .id(1L)
                        .name("teste")
                        .email("test@gmail.com")
                        .password(password)
                        .build();
        User expecteduser = user.toBuilder().password(encodedPass).build();

        Mockito.when(userService.create(user, mockedWalletService)).thenReturn(user);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.email").value(user.getEmail()))
                        .andExpect(jsonPath("$.name").value(user.getName()));
    }
}
