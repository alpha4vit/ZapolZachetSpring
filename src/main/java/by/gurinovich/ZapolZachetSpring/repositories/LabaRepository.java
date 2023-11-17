package by.gurinovich.ZapolZachetSpring.repositories;

import by.gurinovich.ZapolZachetSpring.models.Laba;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.Zachet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabaRepository extends JpaRepository<Laba, Long> {
    Optional<Laba> findByNumberAndSubject(Integer number, Subject subject);
    List<Laba> findAllBySubjectAndZachetsNotContaining(Subject subject, Zachet zachet);
}
