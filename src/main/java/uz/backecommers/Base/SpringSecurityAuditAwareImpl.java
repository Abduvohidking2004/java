package uz.backecommers.Base;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.backecommers.identety.entity.Users;

import java.util.Optional;

public class SpringSecurityAuditAwareImpl implements AuditorAware<Users> {

    @Override
    public Optional<Users> getCurrentAuditor() {
//        return Optional.ofNullable(SecurityContextHolder.getContext())
//                .map(SecurityContext::getAuthentication)
//                .filter(Authentication::isAuthenticated)
//                .map(Authentication::getPrincipal)
//                .map(User.class::cast);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            Users user = (Users) authentication.getPrincipal();
            System.out.println(user.toString());
            return Optional.of(user);
        }
        return Optional.empty();
    }

}
