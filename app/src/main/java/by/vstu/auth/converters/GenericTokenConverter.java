package by.vstu.auth.converters;

import by.vstu.auth.components.converters.BaseTokenConverter;
import by.vstu.auth.components.converters.TokenConvertManager;
import by.vstu.auth.models.UserModel;
import by.vstu.auth.models.tokens.GenericTokenResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class GenericTokenConverter  extends BaseTokenConverter {

    private final TokenConvertManager convertManager;

    public GenericTokenConverter(TokenConvertManager convertManager) {
        super("generic");
        this.convertManager = convertManager;
    }

    @Override
    public GenericTokenResponse convertToken(Jwt jwt, UserModel user) {
        GenericTokenResponse genericTokenResponse = new GenericTokenResponse();
        genericTokenResponse.setToken(jwt.getTokenValue());
        return genericTokenResponse;
    }

    @PostConstruct
    public void init() {
        this.convertManager.registerConverter(this);
    }

}
