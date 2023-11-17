package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.Laba;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.Zachet;
import by.gurinovich.ZapolZachetSpring.repositories.StudentRepository;
import by.gurinovich.ZapolZachetSpring.repositories.ZachetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupService groupService;
    private final ZachetService zachetService;

    public List<Student> getAll(){
        return studentRepository.findAll();
    }

    public Student getById(Long id){
        return studentRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean deleteStudentById(Long id){
        if (getById(id) != null) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void save(Student student, Long groupId){
        student.setGroup(groupService.getById(groupId));
        studentRepository.save(student);
        updateZachetsForNewStudent(groupId, student);
    }


    @Transactional
    public void update(Student student, Long studentId, Long groupId){
        student.setId(studentId);
        student.setGroup(groupService.getById(groupId));
        studentRepository.save(student);
    }

    public Student getByName(String fio){
        return studentRepository.findByFio(fio).orElse(null);
    }


    @Transactional
    public void updateZachetsForNewStudent(Long groupId, Student student){
        groupService.getById(groupId).getSubjects().forEach(subject ->
                subject.getLabas().forEach(laba ->
                        zachetService.addZachetToStudent(student, laba)));
    }
}
