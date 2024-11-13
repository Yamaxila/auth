package by.vstu.auth.controllers;

import by.vstu.auth.components.auth.AuthHelperManager;
import by.vstu.auth.models.BaseResponse;
import by.vstu.auth.models.tokens.GenericTokenResponse;
import by.vstu.auth.models.tokens.OldTokenResponse;
import by.vstu.auth.models.tokens.ServiceTokenModel;
import by.vstu.auth.models.tokens.UserTokenModel;
import by.vstu.auth.services.TokenService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TokenController {

    private final JwtDecoder jwtDecoder;
    private final AuthHelperManager authHelperManager;
    private final TokenService tokenService;


    @RequestMapping(value = "/token",
            produces = {"application/json"},
            method = RequestMethod.POST)
    private ResponseEntity<String> getToken(String username, String password, @RequestParam(required = false, defaultValue = "old") String type) {

        Gson gson = new Gson();

        if (username == null && password == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(new BaseResponse().withMessage("Auth data not provided!")));

        if (username == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(new BaseResponse().withMessage("Username not provided!")));

        if (password == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(new BaseResponse().withMessage("Password not provided!")));

        if(!this.authHelperManager.isRegistered(type))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(new BaseResponse().withMessage("Auth helper " + type + " not registered!")));

        Object o = this.authHelperManager.getHelperByName(type).authenticate(username, password);

        if(o instanceof OldTokenResponse old)
            return ResponseEntity.ok(gson.toJson(old));

        if(o instanceof GenericTokenResponse generic)
            return ResponseEntity.ok(gson.toJson(generic));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(new BaseResponse().withMessage("Generated value not is TokenResponse!")));

    }

    @RequestMapping(value = "/validate",
            produces = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<String> validate(String token) {
        try {
            Jwt jwt = this.jwtDecoder.decode(token);

            if (jwt == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

            if (jwt.getClaimAsBoolean("service") != null && jwt.getClaimAsBoolean("service")) {

                ServiceTokenModel stm = this.tokenService.getServiceToken(UUID.fromString(jwt.getId()));

                if (stm == null)
                    return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body("Invalid serviceToken!");

                if (stm.isBlocked())
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token blocked!");
                return ResponseEntity.ok().build();

            }

            if (jwt.getClaimAsBoolean("service") == null || !jwt.getClaimAsBoolean("service")) {

                if (jwt.getExpiresAt() == null)
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token not contains expires time!");

                if (jwt.getExpiresAt().toEpochMilli() < System.currentTimeMillis())
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired!");

                UserTokenModel utm = this.tokenService.getUserToken(UUID.fromString(jwt.getId()));

                if (utm == null)
                    return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body("Invalid serviceToken!");

                if (utm.isBlocked())
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token blocked!");

                if (utm.getUser() == null || utm.getUser().isLocked())
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User blocked!");
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown token type!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }

    }


    @RequestMapping(value = "/service_token",
            produces = {"application/json"},
            method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'STUDENT')")
    private ResponseEntity<String> getServiceToken(@RequestHeader("Authorization") String token, String serviceId) {

        if (serviceId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("serviceId not provided!");

        if(!this.authHelperManager.isRegistered("service"))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("service helper not registered!");

        Object o = this.authHelperManager.getHelperByName("service").authenticate(token, serviceId);

        if(!(o instanceof String))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("generated value is not String!");


        return ResponseEntity.ok((String) o);
    }


    @RequestMapping(value = "/invalidate",
            produces = {"application/json"},
            method = RequestMethod.POST)
    private ResponseEntity<String> invalidate(@RequestHeader("Authorization") String token) {

        if (token.startsWith("Bearer "))
            token = token.substring(7);

        Jwt jwt = this.jwtDecoder.decode(token);

        if (jwt == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

        this.tokenService.invalidateUserToken(jwt.getId());

        return ResponseEntity.ok().build();
    }


}
