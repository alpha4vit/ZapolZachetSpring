package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.DTO.GroupAndSubject;
import by.gurinovich.ZapolZachetSpring.DTO.Request;
import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.User;
import by.gurinovich.ZapolZachetSpring.services.GroupService;
import by.gurinovich.ZapolZachetSpring.services.SubjectService;
import by.gurinovich.ZapolZachetSpring.services.UserService;
import by.gurinovich.ZapolZachetSpring.services.ZachetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class TableController {
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final ZachetService zachetService;
    private final UserService userService;


    @GetMapping("/choosegroup")
    public String chooseGroup(Model model){
        User user = userService.getAuthenticatedUser();
        model.addAttribute("current_user", user);
        return "users/choosePage";
    }

    @PostMapping("/choosegroup")
    public String showToTable(@RequestBody Request request, Model model) throws IOException {
        User user = userService.getAuthenticatedUser();
        Group group = groupService.getById(request.getGroupId());
        Subject subject = subjectService.getById(request.getSubjectId());
        model.addAttribute("students", group.getStudents())
                .addAttribute("zachetService", zachetService)
                .addAttribute("current_user", user)
                .addAttribute("groupANDsubject", new GroupAndSubject(group, subject));
        return "users/table";
    }


}
