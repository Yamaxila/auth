package by.vstu.auth.converters;

import by.vstu.auth.components.converters.BaseTokenConverter;
import by.vstu.auth.components.converters.TokenConvertManager;
import by.vstu.auth.components.users.UserHelperManager;
import by.vstu.auth.dto.UserDTO;
import by.vstu.auth.models.UserModel;
import by.vstu.auth.models.tokens.OldTokenResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OldTokenConverter extends BaseTokenConverter {

    private final UserHelperManager helperManager;
    private final TokenConvertManager convertManager;

    public OldTokenConverter(UserHelperManager helperManager, TokenConvertManager convertManager) {
        super("old");
        this.helperManager = helperManager;
        this.convertManager = convertManager;
    }

    @Override
    public OldTokenResponse convertToken(Jwt jwt, UserModel user) {
        OldTokenResponse response = new OldTokenResponse();

        response.setMessage("");
        response.setJti(jwt.getId());
        response.setTokenType("bearer");
        response.setAccessToken(jwt.getTokenValue());
        response.setFio(jwt.getClaimAsString("fio"));
        response.setIdFromSource(0L);
        response.setTable("");
        response.setEmail(user.getEmail());
        response.setExpiresIn(Objects.requireNonNull(jwt.getExpiresAt()).toEpochMilli() - Objects.requireNonNull(jwt.getIssuedAt()).toEpochMilli());
        response.setRoles(user.getRolesAsString());
        response.setScope(user.getScopes());

        if(this.helperManager.isRegistered(user.getHelperName())) {
            UserDTO userDTO = this.helperManager.getHelperByName(user.getHelperName()).toUserDTO(user);

            response.setIdFromSource(userDTO.getExternalId());
            response.setTable(userDTO.getHelperName());
            response.setFio(String.format("%s %s %s", userDTO.getSurname(), userDTO.getName(), userDTO.getPatronymic()));
        }

        return response;
    }

    @PostConstruct
    public void init() {
        this.convertManager.registerConverter(this);
    }

}
