package by.vstu.auth.configs;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return jwt.getClaimAsStringList("roles").stream()
                .map(role -> "ROLE_" + role) // Совместимость
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}