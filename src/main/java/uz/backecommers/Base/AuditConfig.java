package uz.backecommers.Base;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import uz.backecommers.identety.entity.Users;


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditConfig {
    @Bean
    AuditorAware<Users> auditorAware(){
        return new SpringSecurityAuditAwareImpl();
    }
}
