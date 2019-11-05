package pw.react.backend.reactbackend.repositories;

import java.util.List;

import pw.react.backend.reactbackend.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findByLogin(String login);
	User findById(int Id);
}