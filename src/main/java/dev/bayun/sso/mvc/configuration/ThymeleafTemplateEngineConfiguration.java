package dev.bayun.sso.mvc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * @author Максим Яськов
 */

@Configuration
public class ThymeleafTemplateEngineConfiguration {

    @Bean
    public ClassLoaderTemplateResolver webClientTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setCacheable(false);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCheckExistence(true);
        templateResolver.setName("client-web");
        templateResolver.setOrder(0);
        templateResolver.setPrefix("client-web/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(webClientTemplateResolver());

        return templateEngine;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");

        return viewResolver;
    }
}
