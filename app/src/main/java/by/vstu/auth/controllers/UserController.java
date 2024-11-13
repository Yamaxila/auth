package by.vstu.auth.controllers;

import by.vstu.auth.components.users.UserHelperManager;
import by.vstu.auth.dto.UserDTO;
import by.vstu.auth.models.UserModel;
import by.vstu.auth.services.TokenService;
import by.vstu.auth.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v2/")
@RequiredArgsConstructor
public class UserController {

    private final UserHelperManager helperManager;
    private final UserService userService;
    private final TokenService tokenService;


    @RequestMapping(value = "/me",
            produces = {"application/json"},
            method = RequestMethod.GET)
    private ResponseEntity<UserDTO> me(@RequestHeader("Authorization") String token) {

        Optional<Jwt> oJwt = this.tokenService.parseToken(token);

        if(oJwt.isEmpty())
            return ResponseEntity.status(500).body((UserDTO) new UserDTO().withMessage("Token decode error!"));

        Optional<UserModel> oUser = this.tokenService.getUser(UUID.fromString(oJwt.get().getId()));

        if (oUser.isEmpty())
            return ResponseEntity.status(404).body((UserDTO) new UserDTO().withMessage("Can't find user by jti!"));

        UserModel user = oUser.get();

        if(user.getHelperName() != null && user.getExternalId() != null && this.helperManager.isRegistered(user.getHelperName())) {
            log.info("Using external data for dto building!");
            return ResponseEntity.ok(this.userService.loadExternalUserData(user));
        }

        UserDTO dto = new UserDTO();

        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setName(user.getUsername());
        dto.setScope(user.getScopes());
        dto.setRoles(user.getRolesAsString());

        return ResponseEntity.ok(dto);
    }



}
