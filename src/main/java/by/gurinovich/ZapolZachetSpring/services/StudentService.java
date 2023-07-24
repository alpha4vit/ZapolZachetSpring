package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.Laba;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.Zachet;
import by.gurinovich.ZapolZachetSpring.repositories.LabaRepository;
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
    private final LabaService labaService;

    @Autowired
    public StudentService(StudentRepository studentRepository, GroupService groupService, SubjectService subjectService, ZachetRepository zachetRepository, LabaService labaService) {
        this.studentRepository = studentRepository;
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.zachetRepository = zachetRepository;
        this.labaService = labaService;
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    @Transactional
    public void deleteStudentById(int id){
        Student student = studentRepository.findById(id).orElse(null);
        zachetRepository.deleteAll(student.getZachety());
        studentRepository.deleteById(id);

    }

    public Student findById(int id){
        return studentRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Student student, int group_id){
        student.setGroup(groupService.findById(group_id));
        studentRepository.save(student);
        updateZachetsForNewStudent(group_id, student);
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


    @Transactional
    public void updateZachetsForNewStudent(Integer group_id, Student student){
        List<Subject> subjects = groupService.findById(group_id).getSubjects();
        for (Subject subject : subjects){
            for (Laba laba : subject.getLabas()){
                zachetRepository.save(new Zachet(student, "-", laba));
            }
        }
    }
}
