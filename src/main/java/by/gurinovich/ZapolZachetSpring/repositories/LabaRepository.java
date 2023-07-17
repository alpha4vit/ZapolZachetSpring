package by.gurinovich.ZapolZachetSpring.repositories;

import by.gurinovich.ZapolZachetSpring.models.Laba;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabaRepository extends JpaRepository<Laba, Integer> {
    Optional<Laba> findByNumberAndSubject(Integer number, Subject subject);
}
