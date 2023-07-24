package by.gurinovich.ZapolZachetSpring.controllers.API;

import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.auth.User;
import by.gurinovich.ZapolZachetSpring.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentAPIController {

    private final StudentService studentService;

    @Autowired
    public StudentAPIController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("")
    public ResponseEntity<List<Student>> getStudents(){
        return new ResponseEntity<>(studentService.getAllStudents(), HttpStatusCode.valueOf(200));
    }
}
