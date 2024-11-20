package uz.backecommers.identety.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.backecommers.identety.entity.Users;
import uz.backecommers.identety.repository.UserRepository;
import uz.backecommers.identety.dto.UserUserDetailsDTO;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserUserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = repository.findByUsername(username);
        return user.map(UserUserDetailsDTO::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
    }
}