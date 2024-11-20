package uz.backecommers.Base;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.backecommers.identety.dto.UserUserDetailsDTO;
import uz.backecommers.identety.entity.Users;
import uz.backecommers.identety.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class BaseService {

    @Autowired
    UserRepository usersRepository;

    public Users getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserUserDetailsDTO) {
            UserUserDetailsDTO userDetailsDTO = (UserUserDetailsDTO) principal;
            Long userId = userDetailsDTO.getId();  //
            return usersRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        throw new RuntimeException("User not authenticated");
    }
}
