package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.Zachet;
import by.gurinovich.ZapolZachetSpring.repositories.StudentRepository;
import by.gurinovich.ZapolZachetSpring.repositories.ZachetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final ZachetRepository zachetRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, GroupService groupService, SubjectService subjectService, ZachetRepository zachetRepository) {
        this.studentRepository = studentRepository;
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.zachetRepository = zachetRepository;
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

        List<Subject> subjects = subjectService.getSubjects();
        for (Subject subject : subjects){
            for (int i =1; i <= subject.getQuantOfLabs(); ++i){
                zachetRepository.save(new Zachet(studentRepository.findByFio(student.getFio()), subject, "-", i));
            }
        }
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
