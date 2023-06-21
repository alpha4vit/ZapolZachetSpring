package by.gurinovich.ZapolZachetSpring.repositories;

import by.gurinovich.ZapolZachetSpring.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findByFio(String fio);
}
