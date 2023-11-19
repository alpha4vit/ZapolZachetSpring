package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupService groupService;
    private final ZachetService zachetService;

    public List<Student> getAll(){
        return studentRepository.findAll().stream().sorted(Comparator.comparing(Student::getFio)).toList();
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
        student.setPerformance(0.0);
        studentRepository.save(student);
        updateZachetsForNewStudent(groupId, student);
    }


    @Transactional
    public void update(Student student, Long studentId, Long groupId){
        Student before = getById(studentId);
        student.setId(studentId);
        student.setPerformance(before.getPerformance());
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

    @Transactional
    public void updatePerfomance(Student student){
        Long count = student.getZachety().stream().filter(zachet -> zachet.getValue().equals("+")).count();
        student.setPerformance(count/(double)student.getZachety().size());
        studentRepository.save(student);
        groupService.updateAveragePerfomance(student.getGroup());
    }
}
