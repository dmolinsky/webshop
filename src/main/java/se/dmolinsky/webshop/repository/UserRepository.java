package se.dmolinsky.webshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.dmolinsky.webshop.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);


}
