package by.vstu.auth.components.auth;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.oauth2.jwt.Jwt;

public interface IAuthHelper<O, R> {

    boolean canHandleRequest(@NotNull String username);

    R authenticate(@NotNull String username, @NotNull String password);

    Jwt generateToken(@NotNull O obj);

    void saveToken(@NotNull Jwt jwt, @NotNull O obj);

}
