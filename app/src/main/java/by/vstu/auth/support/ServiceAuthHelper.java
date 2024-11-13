package by.vstu.auth.support;

import by.vstu.auth.components.auth.AuthHelperManager;
import by.vstu.auth.components.auth.BaseAuthHelper;
import by.vstu.auth.models.tokens.ServiceTokenModel;
import by.vstu.auth.services.TokenService;
import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ServiceAuthHelper extends BaseAuthHelper<String, String> {

    private String token;

    private final TokenService tokenService;
    private final AuthHelperManager authHelperManager;

    public ServiceAuthHelper(TokenService tokenService, AuthHelperManager authHelperManager) {
        super("service");
        this.tokenService = tokenService;
        this.authHelperManager = authHelperManager;
    }

    @Override
    public boolean canHandleRequest(@NotNull String username) {
        return false;
    }

    @Override
    public String authenticate(@NotNull String token, @NotNull String serviceId) {
        this.token = token;
        return this.generateToken(serviceId).getTokenValue();
    }

    @Override
    public Jwt generateToken(@NotNull String serviceId) {

        if(this.token == null)
            throw new IllegalStateException("Token has not been set!");

        Optional<Jwt> jwt = this.tokenService.parseToken(this.token);

        if(jwt.isEmpty())
            throw new IllegalStateException("Invalid token");

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus(20000, ChronoUnit.HOURS))
                .subject(jwt.get().getSubject())
                .claim("request_jti", jwt.get().getId())
                .id(UUID.randomUUID().toString())
                .audience(List.of(serviceId))
                .claim("scope", List.of(serviceId + "_read", serviceId + "_rsql"))
                .claim("authorities", List.of("SERVICE"))
                .claim("roles", List.of("SERVICE"))
                .claim("service", true)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256)
                .type("JWT").build();


        Jwt outToken = this.tokenService.getJwtEncoder().encode(JwtEncoderParameters.from(jwsHeader, claims));

        this.saveToken(outToken, serviceId);


        return outToken;
    }

    @Override
    public void saveToken(@NotNull Jwt jwt, @NotNull String serviceId) {
        ServiceTokenModel stm = new ServiceTokenModel();

        stm.setJti(UUID.fromString(jwt.getId()));
        stm.setCreatedAt(LocalDateTime.now());
        stm.setToken(jwt.getTokenValue());
        stm.setBlocked(false);
        stm.setResourceIds(serviceId);

        this.tokenService.saveServiceToken(stm);
    }

    @PostConstruct
    public void register() {
        this.authHelperManager.registerHelper(this);
    }

}
