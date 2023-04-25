package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.repositories.GroupRepository;
import by.gurinovich.ZapolZachetSpring.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupService groupService;

    @Autowired
    public StudentService(StudentRepository studentRepository, GroupService groupService) {
        this.studentRepository = studentRepository;
        this.groupService = groupService;
    }

    @Transactional
    public void deleteStudentById(int id){
        studentRepository.deleteById(id);
    }

    public Student findById(int id){
        return studentRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Student student, int group_id){
        student.setGroup(groupService.findById(group_id));
        studentRepository.save(student);
    }

    @Transactional
    public void update(Student student, int student_id, int group_id){
        student.setId(student_id);
        student.setGroup(groupService.findById(group_id));
        studentRepository.save(student);
    }

    public Student findByName(String fio){
        return studentRepository.findByFio(fio);
    }
}
