package se.dmolinsky.webshop.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.dmolinsky.webshop.model.User;
import se.dmolinsky.webshop.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public void add(User user) {
        repository.save(user);
    }

    public List<User> getAll() {
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

    public boolean usernameExists(String username) {
        return repository.findByUsername(username).isPresent();
    }

}