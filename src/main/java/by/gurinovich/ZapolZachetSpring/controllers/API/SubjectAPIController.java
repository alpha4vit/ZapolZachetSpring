package by.gurinovich.ZapolZachetSpring.controllers.API;

import by.gurinovich.ZapolZachetSpring.DTO.LabaDTO;
import by.gurinovich.ZapolZachetSpring.models.Laba;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.services.LabaService;
import by.gurinovich.ZapolZachetSpring.services.StudentService;
import by.gurinovich.ZapolZachetSpring.services.SubjectService;
import by.gurinovich.ZapolZachetSpring.utils.mappers.impl.LabaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectAPIController {
    private final SubjectService subjectService;
    private final LabaMapper labaMapper;
    private final LabaService labaService;
    private final StudentService studentService;

    @GetMapping("")
    public ResponseEntity<List<Subject>> getSubjects(){
        return new ResponseEntity<>(subjectService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{subject_id}/labas/{student_id}")
    public ResponseEntity<List<LabaDTO>> getLabasBySubjectAndStudent(@PathVariable("subject_id") Long subjectId,
                                                                     @PathVariable("student_id") Long studentId){
        Subject subject = subjectService.getById(subjectId);
        Student student = studentService.getById(studentId);
        return new ResponseEntity<>(labaMapper.toDTOs(
                labaService.getAllUndoneByStudentAndSubject(student, subject).stream().sorted(Comparator.comparingInt(Laba::getNumber)).toList()),
                HttpStatus.OK);
    }

}
