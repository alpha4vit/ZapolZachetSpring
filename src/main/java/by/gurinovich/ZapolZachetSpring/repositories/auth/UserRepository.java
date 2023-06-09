package by.gurinovich.ZapolZachetSpring.repositories.auth;

import by.gurinovich.ZapolZachetSpring.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name);
    User findByEmail(String email);
    List<User> findByNameStartingWith(String name);
}
