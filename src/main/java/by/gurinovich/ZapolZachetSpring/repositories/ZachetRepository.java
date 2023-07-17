package by.gurinovich.ZapolZachetSpring.repositories;

import by.gurinovich.ZapolZachetSpring.models.Laba;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.Zachet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZachetRepository extends JpaRepository<Zachet, Integer> {
    List<Zachet> findByStudent(Student student);
    Optional<Zachet> findByStudentAndLaba(Student student, Laba laba);
}
