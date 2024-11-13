package by.vstu.auth.models.tokens;

import by.vstu.auth.models.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenericTokenResponse extends BaseResponse {

    private String token;

}
