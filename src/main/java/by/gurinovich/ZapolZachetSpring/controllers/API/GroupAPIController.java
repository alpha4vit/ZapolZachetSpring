package by.gurinovich.ZapolZachetSpring.controllers.API;

import by.gurinovich.ZapolZachetSpring.DTO.GroupDTO;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.auth.User;
import by.gurinovich.ZapolZachetSpring.services.GroupService;
import by.gurinovich.ZapolZachetSpring.services.StudentService;
import by.gurinovich.ZapolZachetSpring.services.SubjectService;
import by.gurinovich.ZapolZachetSpring.services.auth.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupAPIController {
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final UserDetailsService userDetailsService;
    private final StudentService studentService;

    @Autowired
    public GroupAPIController(GroupService groupService, SubjectService subjectService, UserDetailsService userDetailsService, StudentService studentService) {
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.userDetailsService = userDetailsService;
        this.studentService = studentService;
    }

    @GetMapping("")
    public ResponseEntity<List<GroupDTO>> getGroups(){
        return new ResponseEntity<>(groupService.getGroupsDTO(), HttpStatusCode.valueOf(200));
    }



}
