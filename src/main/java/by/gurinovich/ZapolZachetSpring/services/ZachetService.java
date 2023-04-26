package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.Zachet;
import by.gurinovich.ZapolZachetSpring.repositories.ZachetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ZachetService {
    private final ZachetRepository zachetRepository;
    private final StudentService studentService;
    private final SubjectService subjectService;

    @Autowired
    public ZachetService(ZachetRepository zachetRepository, StudentService studentService, SubjectService subjectService) {
        this.zachetRepository = zachetRepository;
        this.studentService = studentService;
        this.subjectService = subjectService;
    }

    @Transactional
    public void save(Zachet zachet){
        zachetRepository.save(zachet);
    }

    public List<Zachet> findByStudentAndSubject(Student student, Subject subject){
        return zachetRepository.findByStudentAndSubject(student, subject).stream().sorted((z1, z2) -> Integer.compare(z1.getNumber(), z2.getNumber())).toList();
    }

    @Transactional
    public void update(Zachet zachet, int subject_id){
        Optional<Zachet> temp = zachetRepository.findByStudentAndSubjectAndNumber(studentService.findById(zachet.getStudent().getId()), subjectService.findById(subject_id), zachet.getNumber());
        if (temp.isPresent()){
            zachet.setId(temp.get().getId());
            zachetRepository.save(zachet);
        }
        zachetRepository.save(zachet);
    }
}
