package uz.sh;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.EntityManagerHolder;

@EntityScan("uz.sh")
@OpenAPIDefinition
@SpringBootApplication
public class JpaCriteriaAndSpecificationsApplication {

    public static void main( String[] args ) {
        SpringApplication.run(JpaCriteriaAndSpecificationsApplication.class, args);
    }
}
