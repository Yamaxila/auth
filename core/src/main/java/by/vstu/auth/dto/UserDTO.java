package by.vstu.auth.dto;

import by.vstu.auth.models.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends BaseResponse {

    @JsonProperty("external_id")
    private Long externalId;
    @JsonProperty("helper_name")
    private String helperName;
    private String username;
    private String surname;
    private String name;
    private String patronymic;
    private String email;
    private List<String> roles;
    private List<String> scope;


}
