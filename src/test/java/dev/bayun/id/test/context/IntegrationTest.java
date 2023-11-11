package dev.bayun.id.test.context;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Максим Яськов
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(RedisContainerTestConfiguration.class)
public @interface IntegrationTest {

}
