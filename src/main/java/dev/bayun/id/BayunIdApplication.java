package dev.bayun.id;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.ssl.SslBundles;

/**
 * @author Максим Яськов
 */

@SpringBootApplication
public class BayunIdApplication {

    public BayunIdApplication(SslBundles sslBundles) {
        System.out.println("TEST 1");
    }

    public static void main(String[] args) {
        SpringApplication.run(BayunIdApplication.class, args);
    }

}
