package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.GroupAndSubject;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.services.GroupService;
import by.gurinovich.ZapolZachetSpring.services.SubjectService;
import by.gurinovich.ZapolZachetSpring.services.ZachetService;
import by.gurinovich.ZapolZachetSpring.utils.tablesCreating.TableWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller

@RequestMapping()
public class UserController {
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final ZachetService zachetService;
    private final TableWriter tableWriter;

    @Autowired
    public UserController(GroupService groupService, SubjectService subjectService, ZachetService zachetService, TableWriter tableWriter) {
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.zachetService = zachetService;
        this.tableWriter = tableWriter;
    }

    @GetMapping("/choosegroup")
    public String chooseGroup(Model model, @ModelAttribute("groupANDsubject") GroupAndSubject groupAndSubject){
        model.addAttribute("groups", groupService.getGroups())
                .addAttribute("subjects", subjectService.getSubjects());
        return "users/choosePage";
    }

    @PostMapping("/choosegroup")
    public String showToTable(@ModelAttribute("groupANDsubject") GroupAndSubject groupAndSubject, Model model) throws IOException {
        Group groupInfo = groupService.findById(groupAndSubject.getGroup().getId());
        Subject subject = subjectService.findById(groupAndSubject.getSubject().getId());

        model.addAttribute("groups", groupService.getGroups())
                .addAttribute("subjects", subjectService.getSubjects())
                .addAttribute("groupInfo", groupInfo)
                .addAttribute("students", groupInfo.getStudents())
                .addAttribute("quantOfLabas", subject.getQuantOfLabs())
                .addAttribute("zachetService", zachetService);


        return "users/showGroupInfo";
    }
}
