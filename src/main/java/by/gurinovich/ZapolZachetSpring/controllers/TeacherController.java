package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.models.*;
import by.gurinovich.ZapolZachetSpring.security.UserDetails;
import by.gurinovich.ZapolZachetSpring.services.GroupService;
import by.gurinovich.ZapolZachetSpring.services.StudentService;
import by.gurinovich.ZapolZachetSpring.services.SubjectService;
import by.gurinovich.ZapolZachetSpring.services.ZachetService;
import by.gurinovich.ZapolZachetSpring.utils.validotors.ZachetValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final ZachetService zachetService;
    private final ZachetValidator zachetValidator;

    @Autowired
    public TeacherController(GroupService groupService, SubjectService subjectService, StudentService studentService, ZachetService zachetService, ZachetValidator zachetValidator) {
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.studentService = studentService;
        this.zachetService = zachetService;
        this.zachetValidator = zachetValidator;
    }

    @GetMapping()
    public String chooseGroupPage(Model model, @ModelAttribute("groupANDsubject") GroupAndSubject groupAndSubject) {
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model.addAttribute("groups", groupService.getGroups())
                .addAttribute("subjects", subjectService.getSubjects())
                .addAttribute("user", user);
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

    @PatchMapping("/group/newZachet")
    public String newZachet(@RequestParam("group_id") Integer group_id, @RequestParam("subject_id") Integer subject_id, @ModelAttribute("zachetModel") @Valid ZachetModel zachetModel, BindingResult bindingResult, Model model){
        Group group = groupService.findById(group_id);
        Subject subject = subjectService.findById(subject_id);

        Zachet zachet = new Zachet(studentService.findById(zachetModel.getStudent().getId()), subject, zachetModel.getValue(), zachetModel.getNumber());
        zachetValidator.validate(zachet, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("group", group)
                    .addAttribute("groupANDsubject", new GroupAndSubject(group, subject))
                    .addAttribute("subject", subject)
                    .addAttribute("students", group.getStudents())
                    .addAttribute("groups", groupService.getGroups())
                    .addAttribute("subjects", subjectService.getSubjects())
                    .addAttribute("zachetService", zachetService);
            return "teachers/groupInfo";
        }


        zachetService.update(zachet, subject_id);
        return String.format("redirect:/teacher/group?group=%d&subject=%d", group_id, subject_id);
    }
}
