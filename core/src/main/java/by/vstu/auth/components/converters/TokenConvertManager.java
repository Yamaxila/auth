package by.vstu.auth.components.converters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TokenConvertManager {

    public static final List<BaseTokenConverter> converters = new ArrayList<>();

    public void registerConverter(BaseTokenConverter converter) {
        log.info("Registering converter {}", converter.getName());
        if(this.isRegistered(converter.getName()))
            throw new IllegalArgumentException("Converter " + converter.getName() + " already registered");

        converters.add(converter);
    }

    public void unregisterConverter(BaseTokenConverter converter) {
        if(!this.isRegistered(converter.getName()))
            throw new IllegalStateException("Converter " + converter.getName() + " is not registered");
        converters.remove(converter);
    }

    public BaseTokenConverter getConverterByName(String name) {
        return converters.stream().filter(converter -> converter.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean isRegistered(String name) {
        return converters.stream().anyMatch(converter -> converter.getName().equals(name));
    }



}
