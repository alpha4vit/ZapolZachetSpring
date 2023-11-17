package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.Laba;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.repositories.LabaRepository;
import by.gurinovich.ZapolZachetSpring.repositories.ZachetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LabaService {
    private final LabaRepository labaRepository;
    private final ZachetRepository zachetRepository;
    private final SubjectService subjectService;
    private final ZachetService zachetService;


    public Laba getById(Long id){
        return labaRepository.findById(id).orElse(null);
    }

    public Laba getByNumberAndSubject(Integer number, Subject subject){
        return labaRepository.findByNumberAndSubject(number, subject).orElse(null);
    }

    public List<Laba> getAllUndoneByStudentAndSubject(Student student, Subject subject){
        List<Laba> result = new ArrayList<>();
        student.getZachety().forEach(el -> result.addAll(labaRepository.findAllBySubjectAndZachetsNotContaining(subject, el)));
        result.forEach(System.out::println);
        return result;
    }

    @Transactional
    public Laba save(Laba laba){
        Laba newLaba = addLabaToSubjects(laba);
        addZachetsToStudents(laba);
        return newLaba;
    }

    @Transactional
    public Laba addLabaToSubjects(Laba laba){
        Laba newLaba = labaRepository.save(laba);
        Subject subject = laba.getSubject();
        List<Laba> labas = subject.getLabas();
        labas.add(newLaba);
        subject.setLabas(labas);
        subjectService.save(subject);
        return newLaba;
    }

    @Transactional
    public void addZachetsToStudents(Laba laba){
        laba.getSubject().getGroups().forEach(group ->
                group.getStudents().forEach(student ->
                        zachetService.addZachetToStudent(student, laba)));
    }

    @Transactional
    public void deleteById(Long labaId){
        Laba laba =  getById(labaId);
        if (laba != null) {
            zachetRepository.deleteAllByLaba(laba);
            labaRepository.delete(laba);
        }
    }

}
