package by.vstu.auth.repo;

import by.vstu.auth.models.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Integer> {

    Optional<UserModel> findByEmail(String email);

    Optional<UserModel> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);
}