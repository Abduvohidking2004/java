package uz.backecommers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackEcommersApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackEcommersApplication.class, args);
    }

}
