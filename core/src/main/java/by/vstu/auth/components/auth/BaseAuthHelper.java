package by.vstu.auth.components.auth;

import lombok.Getter;

@Getter
public abstract class BaseAuthHelper<T, R>  implements IAuthHelper<T, R>  {

    protected String name;

    public BaseAuthHelper(String name) {
        this.name = name;
    }

}
