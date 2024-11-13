package by.vstu.auth.repo;

import by.vstu.auth.models.tokens.ServiceTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceTokenRepository extends JpaRepository<ServiceTokenModel, UUID> {
}
