package ru.geekbrains.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.geekbrains.persist.Role;
import ru.geekbrains.persist.User;
import ru.geekbrains.repository.RoleRepository;
import ru.geekbrains.repository.UserRepository;

import java.util.Set;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class LoginResourceTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private SimpMessagingTemplate webSocketTemplate;

    private static final UUID adminId = UUID.randomUUID();

    private static final UUID userId = UUID.randomUUID();

    private static final UUID adminRoleId = UUID.randomUUID();

    private static final UUID userRoleId = UUID.randomUUID();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userRepository.save(new User(adminId,
                "Admin",
                "Admin",
                "admin@mail.ru",
                "$2a$12$Nu0r.voTxDTA4ixldRI0P.RxkKdHcK4AUsKXu7rqSEOoPvREKjtaS",
                Set.of(
                        roleRepository.save(new Role(adminRoleId, "ROLE_ADMIN"))
                )));
        userRepository.save(new User(userId,
                "User",
                "User",
                "user@mail.ru",
                "$2a$12$EHV5GGgrnVF8WTvdDR06kukY9/9UoH5qDWNI0y4B3eZvRggu0BhZm",
                Set.of(
                        roleRepository.save(new Role(userRoleId, "ROLE_USER"))
                )));
    }

    @Test
    public void testLoginAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/login")
                        .with(httpBasic("admin@mail.ru", "admin")))
                .andExpect(authenticated().withRoles("ADMIN"))
                .andExpect(authenticated().withUsername("admin@mail.ru"));
    }

    @Test
    public void testLoginUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/login")
                        .with(httpBasic("user@mail.ru", "user")))
                .andExpect(authenticated().withRoles("USER"))
                .andExpect(authenticated().withUsername("user@mail.ru"));
    }

    @Test
    public void testLoginUnauthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/login")
                        .with(httpBasic("admin@mail.ru", "")))
                .andExpect(unauthenticated());
    }


}
