package by.vstu.auth.services;

import by.vstu.auth.models.UserModel;
import by.vstu.auth.models.tokens.ServiceTokenModel;
import by.vstu.auth.models.tokens.UserTokenModel;
import by.vstu.auth.repo.ServiceTokenRepository;
import by.vstu.auth.repo.UserTokenRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Getter
    private final JwtEncoder jwtEncoder;
    @Getter
    private final JwtDecoder jwtDecoder;
    private final ServiceTokenRepository serviceTokenRepo;
    private final UserTokenRepository userTokenRepo;

    public void saveServiceToken(ServiceTokenModel serviceToken) {
        this.serviceTokenRepo.saveAndFlush(serviceToken);
    }

    public void saveUserToken(UserTokenModel userTokenModel) {
        this.userTokenRepo.saveAndFlush(userTokenModel);
    }

    public UserTokenModel getUserToken(UUID jti) {
        return this.userTokenRepo.findById(jti).orElse(null);
    }

    public ServiceTokenModel getServiceToken(UUID jti) {
        return this.serviceTokenRepo.findById(jti).orElse(null);
    }

    public void invalidateUserToken(String jti) {
        UserTokenModel utm = this.userTokenRepo.findById(UUID.fromString(jti))
                // Если мы тут словили exception, то у нас явно есть косяк в безопасности
                .orElseThrow(() -> new SecurityException("Invalid token"));
        utm.setBlocked(true);

        this.saveUserToken(utm);
    }

    public Optional<Jwt> parseToken(@NotNull String token) {
        if (token.startsWith("Bearer "))
            token = token.substring(7);

        try {
            return Optional.of(this.jwtDecoder.decode(token));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    public Optional<UserModel> getUser(UUID jti) {
        return this.userTokenRepo.findByJti(jti).map(UserTokenModel::getUser);
    }

}
