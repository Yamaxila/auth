package by.vstu.auth.components.converters;

import by.vstu.auth.models.UserModel;
import lombok.Getter;
import org.springframework.security.oauth2.jwt.Jwt;

@Getter
public abstract class BaseTokenConverter {

    protected String name;

    protected BaseTokenConverter(String name) {
        this.name = name;
    }

    public abstract Object convertToken(Jwt jwt, UserModel user);

}
