package by.vstu.auth.models.tokens;

import by.vstu.auth.models.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OldTokenResponse extends BaseResponse {



    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("email")
    private String email;
    @JsonProperty("fio")
    private String fio;
    @JsonProperty("id_from_source")
    private Long idFromSource;
    @JsonProperty("table")
    private String table;
    @JsonProperty("jti")
    private String jti;
    @JsonProperty("roles")
    private List<String> roles;
    @JsonProperty("scope")
    private List<String> scope;
    @JsonProperty("expires_in")
    private Long expiresIn;


}
