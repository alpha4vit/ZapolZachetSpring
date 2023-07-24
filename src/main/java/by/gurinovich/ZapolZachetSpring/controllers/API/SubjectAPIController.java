package by.gurinovich.ZapolZachetSpring.controllers.API;

import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.auth.User;
import by.gurinovich.ZapolZachetSpring.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectAPIController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectAPIController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("")
    public ResponseEntity<List<Subject>> getSubjects(){
        return new ResponseEntity<>(subjectService.getSubjects(), HttpStatusCode.valueOf(200));
    }

}
