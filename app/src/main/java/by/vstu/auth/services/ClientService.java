package by.vstu.auth.services;

import by.vstu.auth.models.ClientModel;
import by.vstu.auth.repo.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientModel authenticate(String headerString) {

        if(headerString == null || !headerString.startsWith("Basic"))
            return null;

        String decryptedHeader = new String(Base64.getDecoder().decode(headerString.split("Basic ")[1].trim()));

        if(!decryptedHeader.contains(":"))
            return null;

        Optional<ClientModel> oClientModel = this.clientRepository.findByClientId(decryptedHeader.split(":")[0]);

        if(oClientModel.isEmpty())
            return null;

        ClientModel clientModel = oClientModel.get();

        if(this.passwordEncoder.matches(decryptedHeader.split(":")[1], clientModel.getClientSecret()))
            return clientModel;

        return null;
    }

}
