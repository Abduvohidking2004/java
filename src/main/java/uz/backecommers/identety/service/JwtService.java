package uz.backecommers.identety.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(String userName);

    String extractUsername(String token);

    Boolean validateToken(String token, UserDetails userDetails);
}