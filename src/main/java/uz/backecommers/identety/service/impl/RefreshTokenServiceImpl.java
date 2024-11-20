package uz.backecommers.identety.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import uz.backecommers.identety.entity.RefreshToken;
import uz.backecommers.identety.entity.Users;
import uz.backecommers.identety.repository.RefreshTokenRepository;
import uz.backecommers.identety.repository.UserRepository;
import uz.backecommers.identety.service.RefreshTokenService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

    RefreshTokenRepository refreshTokenRepository;
    UserRepository userInfoRepository;
    public static final int tokenExpire = 86400000; // 1 day

    public RefreshToken createRefreshToken(String username) {
        Users user = userInfoRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByUser(user);
        RefreshToken refreshToken;
        if (refreshTokenOptional.isPresent()) {
            refreshTokenOptional.get().setRefreshToken(UUID.randomUUID().toString());
            refreshTokenOptional.get().setExpiryDate(Instant.now().plusMillis(tokenExpire));
            refreshToken = refreshTokenRepository.save(refreshTokenOptional.get());
        } else {
            refreshToken = refreshTokenRepository.save(RefreshToken.builder()
                    .user(user)
                    .refreshToken(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(tokenExpire))
                    .build());
        }
        return refreshToken;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getRefreshToken() + " Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
}