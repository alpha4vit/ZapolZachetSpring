package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.DTO.GroupAndSubject;
import by.gurinovich.ZapolZachetSpring.DTO.Request;
import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.auth.User;
import by.gurinovich.ZapolZachetSpring.security.UserDetails;
import by.gurinovich.ZapolZachetSpring.services.GroupService;
import by.gurinovich.ZapolZachetSpring.services.SubjectService;
import by.gurinovich.ZapolZachetSpring.services.ZachetService;
import by.gurinovich.ZapolZachetSpring.services.auth.RegistrationService;
import by.gurinovich.ZapolZachetSpring.utils.validotors.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping()
public class UserController {
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final ZachetService zachetService;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;

    @Autowired
    public UserController(GroupService groupService, SubjectService subjectService, ZachetService zachetService, UserValidator userValidator, RegistrationService registrationService) {
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.zachetService = zachetService;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/choosegroup")
    public String chooseGroup(Model model){
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model.addAttribute("current_user", user);
        return "users/choosePage";
    }

    @PostMapping("/choosegroup")
    public String showToTable(@RequestBody Request request, Model model) throws IOException {
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        Group group = groupService.findById(request.getGroup_id());
        Subject subject = subjectService.findById(request.getSubject_id());
        model.addAttribute("students", group.getStudents())
                .addAttribute("quantOfLabas", subject.getQuantOfLabs())
                .addAttribute("zachetService", zachetService)
                .addAttribute("current_user", user)
                .addAttribute("groupANDsubject", new GroupAndSubject(group, subject));
        return "users/table";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user){
        return "users/registrationPage";
    }


    @PostMapping("/registration")
    public String registrationPage(@ModelAttribute("user") @Valid User user, BindingResult bindingResult){
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(System.out::println);
            return "users/registrationPage";
        }
        registrationService.register(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String showUserProfile(Model model){
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails)
            user = (UserDetails) principal;
        model.addAttribute("user", user);
        return "general/profile";
    }
}
