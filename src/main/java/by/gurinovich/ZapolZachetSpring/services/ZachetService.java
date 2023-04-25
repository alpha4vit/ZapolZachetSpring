package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.Zachet;
import by.gurinovich.ZapolZachetSpring.repositories.ZachetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ZachetService {
    private final ZachetRepository zachetRepository;

    @Autowired
    public ZachetService(ZachetRepository zachetRepository) {
        this.zachetRepository = zachetRepository;
    }

    @Transactional
    public void save(Zachet zachet){
        zachetRepository.save(zachet);
    }

    public List<Zachet> findByStudentAndSubject(Student student, Subject subject){
        return zachetRepository.findByStudentAndSubject(student, subject);
    }
}
