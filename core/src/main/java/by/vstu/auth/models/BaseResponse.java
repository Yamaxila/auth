package by.vstu.auth.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {

    protected String message;

    public BaseResponse withMessage(String message) {
        this.setMessage(message);
        return this;
    }

}
