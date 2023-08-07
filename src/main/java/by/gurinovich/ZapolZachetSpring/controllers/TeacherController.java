package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.DTO.GroupAndSubject;
import by.gurinovich.ZapolZachetSpring.DTO.Request;
import by.gurinovich.ZapolZachetSpring.models.*;
import by.gurinovich.ZapolZachetSpring.security.UserDetails;
import by.gurinovich.ZapolZachetSpring.services.*;
import by.gurinovich.ZapolZachetSpring.utils.validotors.ZachetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final ZachetService zachetService;
    private final LabaService labaService;

    @Autowired
    public TeacherController(GroupService groupService, SubjectService subjectService, StudentService studentService, ZachetService zachetService, ZachetValidator zachetValidator, LabaService labaService) {
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.studentService = studentService;
        this.zachetService = zachetService;
        this.labaService = labaService;
    }

    @GetMapping()
    public String chooseGroupPage(Model model, @ModelAttribute("groupANDsubject") GroupAndSubject groupAndSubject) {
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model.addAttribute("user", user);
        return "teachers/choosePage";
    }

    @GetMapping("/group")
    public String showGroup(@ModelAttribute("groupANDsubject") GroupAndSubject groupAndSubject, Model model){
        Group group = groupService.findById(groupAndSubject.getGroup().getId());
        Subject subject = subjectService.findById(groupAndSubject.getSubject().getId());
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model.addAttribute("group", group)
                .addAttribute("subject", subject)
                .addAttribute("students", group.getStudents())
                .addAttribute("groups", groupService.getGroups())
                .addAttribute("subjects", subjectService.getSubjects())
                .addAttribute("zachetModel", new ZachetModel())
                .addAttribute("zachetService", zachetService)
                .addAttribute("current_user", user);
        return "teachers/groupInfo";
    }

    @PostMapping("/group/newZachet")
    public String newZachet(@RequestBody Request request, BindingResult bindingResult, Model model){
        Group group = groupService.findById(request.getGroup_id());
        Subject subject = subjectService.findById(request.getSubject_id());
        Student student = studentService.findById(request.getStudent_id());
        zachetService.update(new Zachet(student,
                 request.getValue(), labaService.findById(request.getNewZachetLabaId())));
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
        Group group = groupService.findById(request.getGroup_id());
        Subject subject = subjectService.findById(request.getSubject_id());
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
