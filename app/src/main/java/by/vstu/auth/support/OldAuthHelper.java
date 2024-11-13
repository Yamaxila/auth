package by.vstu.auth.support;

import by.vstu.auth.components.auth.AuthHelperManager;
import by.vstu.auth.components.auth.BaseAuthHelper;
import by.vstu.auth.components.converters.TokenConvertManager;
import by.vstu.auth.models.UserModel;
import by.vstu.auth.models.tokens.OldTokenResponse;
import by.vstu.auth.models.tokens.UserTokenModel;
import by.vstu.auth.services.AuthService;
import by.vstu.auth.services.TokenService;
import by.vstu.auth.services.UserService;
import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component

public class OldAuthHelper extends BaseAuthHelper<UserModel, OldTokenResponse> {

    private final UserService userService;
    private final AuthService authService;
    private final TokenService tokenService;
    private final TokenConvertManager tokenConvertManager;
    private final AuthHelperManager authHelperManager;

    public OldAuthHelper(UserService userService, AuthService authService, TokenService tokenService, TokenConvertManager tokenConvertManager, AuthHelperManager authHelperManager) {
        super("old");
        this.userService = userService;
        this.authService = authService;
        this.tokenService = tokenService;
        this.tokenConvertManager = tokenConvertManager;
        this.authHelperManager = authHelperManager;
    }

    @Override
    public boolean canHandleRequest(@NotNull String username) {
        return this.userService.loadUserByUsername(username) != null;
    }

    @Override
    public OldTokenResponse authenticate(@NotNull String username, @NotNull String password) {
        Authentication authentication = this.authService.authenticate(username, password);

        if(!authentication.isAuthenticated())
            return null;

        UserModel user = (UserModel) authentication.getPrincipal();

        if (user == null)
            return (OldTokenResponse) new OldTokenResponse().withMessage("Username or password incorrect!");

        Jwt jwtToken = this.generateToken(user);

        if (jwtToken == null)
            return (OldTokenResponse) new OldTokenResponse().withMessage("Token generation error!");

        if(!this.tokenConvertManager.isRegistered(this.name))
            return (OldTokenResponse) new OldTokenResponse().withMessage("Token converter is not registered!");

        return (OldTokenResponse) this.tokenConvertManager.getConverterByName(this.name).convertToken(jwtToken, user);
    }

    @Override
    public Jwt generateToken(@NotNull UserModel user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(user.getUsername())
                .id(UUID.randomUUID().toString())
                .audience(user.getResourceIds())
                .claim("scope", user.getScopes())
                .claim("user_name", user.getUsername())
                .claim("fio", user.getUsername())
                .claim("email", user.getEmail())
                .claim("authorities", user.getAuthorities())
                .claim("roles", user.getRolesAsString())
                .build();
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256)
                .type("JWT").build();

        Jwt outToken = this.tokenService.getJwtEncoder().encode(JwtEncoderParameters.from(jwsHeader, claims));

        this.saveToken(outToken, user);

        return outToken;
    }

    @Override
    public void saveToken(@NotNull Jwt jwt, @NotNull UserModel user) {
        UserTokenModel utm = new UserTokenModel();

        utm.setJti(UUID.fromString(jwt.getId()));
        utm.setCreatedAt(LocalDateTime.now());
        utm.setExpiresAt(LocalDateTime.now().plusHours(10));
        utm.setToken(jwt.getTokenValue());
        utm.setUser(user);
        utm.setBlocked(false);

        this.tokenService.saveUserToken(utm);
    }

    @PostConstruct
    public void register() {
        this.authHelperManager.registerHelper(this);
    }
}