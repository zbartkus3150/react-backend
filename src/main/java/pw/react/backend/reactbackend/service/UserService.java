package pw.react.backend.reactbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.reactbackend.model.User;
import pw.react.backend.reactbackend.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private UserRepository UserRepository;

    @Autowired
    public UserService(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    public List<User> findAll() {
        return UserRepository.findAll();
    }

    public List<User> findByLogin(String login) {
        return UserRepository.findByLogin(login);
    }

    public User findById(int id) {
        return UserRepository.findById(id);
    }

    public User save(User user) {
        return UserRepository.save(user);
    }

    public void delete(User user) {
        UserRepository.delete(user);
    }

    public boolean exists(User user) {
        List<User> result = UserRepository.findByLogin(user.getLogin());
        return result != null && !result.isEmpty();
    }
}