package by.vstu.auth.components.users;

import by.vstu.auth.components.BaseRequest;
import by.vstu.auth.dto.UserDTO;
import by.vstu.auth.models.UserModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@Setter
@Getter
public abstract class BaseUserHelper {

    protected final String url;
    protected final String token;
    protected BaseRequest<String> request;

    protected BaseUserHelper(String url, String token) {
        this.url = url;
        this.token = token;
        this.request = new BaseRequest<>(this.url);
        this.request.setMethod(HttpMethod.GET);
        this.request.setMediaType(MediaType.APPLICATION_JSON);
        this.request.setToken(this.token);
    }

    public abstract String getName();

    public abstract UserDTO toUserDTO(UserModel userModel);

}
