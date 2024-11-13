package by.vstu.auth.components.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AuthHelperManager {

    public static final List<BaseAuthHelper<?, ?>> helpers = new ArrayList<>();

    public void registerHelper(BaseAuthHelper<?, ?> helper) {
        log.info("Registering auth helper {}", helper.getName());
        if(this.isRegistered(helper.getName()))
            throw new IllegalArgumentException("Helper " + helper.getName() + " already registered");

        helpers.add(helper);
    }

    public void unregisterHelper(BaseAuthHelper<?, ?> helper) {
        if(!this.isRegistered(helper.getName()))
            throw new IllegalStateException("Helper " + helper.getName() + " is not registered");
        helpers.remove(helper);
    }

    public BaseAuthHelper<?, ?> getHelperByName(String name) {
        return helpers.stream().filter(helper -> helper.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean isRegistered(String name) {
        return helpers.stream().anyMatch(helper -> helper.getName().equals(name));
    }


}
