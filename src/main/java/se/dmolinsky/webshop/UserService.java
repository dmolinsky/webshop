package se.dmolinsky.webshop;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    void add(User user) {
        repository.save(user);
    }

    List<User> getAll() {
        return repository.findAll();
    }

    public Optional<User> getByUsername(String username) {
        return repository.findByUsername(username);
    }

    public void delete(int id) {
        repository.deleteById((long) id);
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

}