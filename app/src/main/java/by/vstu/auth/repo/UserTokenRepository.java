package by.vstu.auth.repo;

import by.vstu.auth.models.tokens.UserTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserTokenRepository extends JpaRepository<UserTokenModel, UUID> {

    Optional<UserTokenModel> findByJti(UUID jti);

}
