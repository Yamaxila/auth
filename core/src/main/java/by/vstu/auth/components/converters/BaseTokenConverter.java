package by.vstu.auth.components.converters;

import by.vstu.auth.models.UserModel;
import org.springframework.security.oauth2.jwt.Jwt;

public abstract class BaseTokenConverter {

    protected String name;

    protected BaseTokenConverter(String name) {
        this.name = name;
    }

    public abstract Object convertToken(Jwt jwt, UserModel user);

    public String getName() {
        return this.name;
    }

}
