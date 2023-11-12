package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.DTO.GroupAndSubject;
import by.gurinovich.ZapolZachetSpring.DTO.Request;
import by.gurinovich.ZapolZachetSpring.models.*;
import by.gurinovich.ZapolZachetSpring.services.*;
import by.gurinovich.ZapolZachetSpring.utils.validotors.ZachetValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final ZachetService zachetService;
    private final LabaService labaService;
    private final UserService userService;


    @GetMapping()
    public String chooseGroupPage(Model model, @ModelAttribute("groupANDsubject") GroupAndSubject groupAndSubject) {
        User user = userService.getAuthenticatedUser();
        model.addAttribute("user", user);
        return "teachers/choosePage";
    }

    @GetMapping("/group")
    public String showGroup(@ModelAttribute("groupANDsubject") GroupAndSubject groupAndSubject, Model model){
        Group group = groupService.getById(groupAndSubject.getGroup().getId());
        Subject subject = subjectService.getById(groupAndSubject.getSubject().getId());
        User user = userService.getAuthenticatedUser();
        model.addAttribute("group", group)
                .addAttribute("subject", subject)
                .addAttribute("students", group.getStudents())
                .addAttribute("groups", groupService.getAll())
                .addAttribute("subjects", subjectService.getAll())
                .addAttribute("zachetModel", new ZachetModel())
                .addAttribute("zachetService", zachetService)
                .addAttribute("current_user", user);
        return "teachers/groupInfo";
    }

    @PostMapping("/group/zachets/new")
    public String newZachet(@RequestBody Request request, BindingResult bindingResult, Model model){
        Group group = groupService.getById(request.getGroupId());
        Subject subject = subjectService.getById(request.getSubjectId());
        Student student = studentService.getById(request.getStudentId());
        zachetService.update(Zachet.builder()
                        .student(student)
                        .value(request.getValue())
                        .laba(labaService.getById(request.getNewZachetLabaId()))
                .build());
        List<Student> students = group.selectStudentsByFilter(request.getSurnameSearch(), request.getLabaNumFilter(), subject);
        if (students == null)
            students = group.getStudents();
        model.addAttribute("groupANDsubject", new GroupAndSubject(group, subject))
                .addAttribute("students", students)
                .addAttribute("zachetService", zachetService)
                .addAttribute("request", request)
                .addAttribute("zachetModel", new ZachetModel(student));
        return "users/table";
    }

    @PostMapping("group/select")
    public String selectUser(Model model, @RequestBody Request request){
        Group group = groupService.getById(request.getGroupId());
        Subject subject = subjectService.getById(request.getSubjectId());
        List<Student> students = group.selectStudentsByFilter(request.getSurnameSearch(), request.getLabaNumFilter(), subject);
        if (students == null)
            students = group.getStudents();
        model.addAttribute("groupANDsubject", new GroupAndSubject(group, subject))
                .addAttribute("students", students)
                .addAttribute("zachetService", zachetService)
                .addAttribute("zachetModel", new ZachetModel())
                .addAttribute("request", request);
        if (request.getLabaNumFilter() != null || request.getSurnameSearch() != null){
            return "users/table";
        }
        return "teachers/groupInfo";
    }

}
