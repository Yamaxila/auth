package by.vstu.auth.services;

import by.vstu.auth.components.users.BaseUserHelper;
import by.vstu.auth.components.users.UserHelperManager;
import by.vstu.auth.dto.UserDTO;
import by.vstu.auth.models.UserModel;
import by.vstu.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserHelperManager helperManager;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(username, username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDTO loadExternalUserData(UserModel user) {
        if(!this.helperManager.isRegistered(user.getHelperName()))
            return null;

        BaseUserHelper helper = this.helperManager.getHelperByName(user.getHelperName());
        return helper.toUserDTO(user);

    }


}
