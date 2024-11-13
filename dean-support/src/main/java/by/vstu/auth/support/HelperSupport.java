package by.vstu.auth.support;

import by.vstu.auth.components.users.UserHelperManager;
import by.vstu.auth.support.helpers.StudentHelper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelperSupport {

    @Value("${dean.token}")
    private String deanServiceToken;
    @Value("${dean.api.url}")
    private String deanApiUrl;

    private final UserHelperManager helperManager;

    @PostConstruct
    public void init() {

        this.helperManager.registerHelper(new StudentHelper(this.deanApiUrl + "students/%s/", this.deanServiceToken));

    }


}
