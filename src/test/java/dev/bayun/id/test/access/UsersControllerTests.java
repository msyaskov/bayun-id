package dev.bayun.id.test.access;

import dev.bayun.sdk.security.AuthenticatedPrincipal;
import dev.bayun.id.test.context.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

/**
 * @author Максим Яськов
 */
@IntegrationTest
public class UsersControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{userId}", UUID.randomUUID())
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.opaqueToken().principal(AuthenticatedPrincipal.builder()
                        .id(UUID.randomUUID())
                        .username("testUsername")
                        .build())));
    }

}
