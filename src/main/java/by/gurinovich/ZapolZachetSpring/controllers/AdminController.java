package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.auth.User;
import by.gurinovich.ZapolZachetSpring.security.UserDetails;
import by.gurinovich.ZapolZachetSpring.services.GroupService;
import by.gurinovich.ZapolZachetSpring.services.StudentService;
import by.gurinovich.ZapolZachetSpring.services.auth.UserDetailsService;
import by.gurinovich.ZapolZachetSpring.utils.validotors.GroupValidator;
import by.gurinovich.ZapolZachetSpring.utils.validotors.StudentValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final GroupService groupService;
    private final GroupValidator groupValidator;
    private final StudentService studentService;
    private final StudentValidator studentValidator;
    private final UserDetailsService userDetailsService;


    @Autowired
    public AdminController(GroupService groupService, GroupValidator groupValidator, StudentService studentService, StudentValidator studentValidator, UserDetailsService userDetailsService) {
        this.groupService = groupService;
        this.groupValidator = groupValidator;
        this.studentService = studentService;
        this.studentValidator = studentValidator;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping()
    public String showAdminPage(Model model, @ModelAttribute("group") Group group){
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model
                .addAttribute("groups", groupService.getGroups())
                .addAttribute("current_user", user);
        return "admin/adminMainPage";
    }

    @PatchMapping("/deleteGroup/{id}")
    public String deleteGroup(@PathVariable("id") int id){
        groupService.deleteById(id);
        return "redirect:/admin";
}

    @PostMapping("/createNewGroup")
    public String createNewGroup(@ModelAttribute("group") @Valid Group group, BindingResult bindingResult, Model model){
        groupValidator.validate(group, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.getGroups());
            return "admin/adminMainPage";
        }
        groupService.save(group);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String getInfoAboutGroup(@PathVariable("id") int id, Model model, @ModelAttribute("newStudent") Student student){
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model
                .addAttribute("group", groupService.findById(id))
                .addAttribute("current_user", user);
        return "admin/groupInfo";
    }

    @PostMapping("/{id}/addStudent")
    public String addStudent(@PathVariable("id") int id, @ModelAttribute("newStudent") @Valid Student student, BindingResult bindingResult, Model model){
        studentValidator.validate(student, bindingResult);
        if (bindingResult.hasErrors()){
            model.addAttribute("group", groupService.findById(id));
            return "admin/groupInfo";
        }
        studentService.save(student, id);
        return String.format("redirect:/admin/%d", id);

    }

    @PostMapping("/{group_id}/{student_id}/deleteStudent")
    public String deleteStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id){
        studentService.deleteStudentById(student_id);
        return String.format("redirect:/admin/%d", group_id);
    }

    @GetMapping("/{group_id}/{student_id}/editStudent")
    public String editStudentPage(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id, Model model){
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model.addAttribute("group_id", group_id)
                .addAttribute("student", studentService.findById(student_id))
                .addAttribute("student_id", student_id)
                .addAttribute("current_user", user);
        return "admin/studentEditPage";
    }

    @PatchMapping("/{group_id}/{student_id}/editStudent")
    public String editStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id, @ModelAttribute("student") @Valid Student student, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "admin/studentEditPage";
        }
        studentService.update(student, student_id, group_id);
        return String.format("redirect:/admin/%d", group_id);
    }


    @GetMapping("/users")
    public String getUsers(Model model){
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model.addAttribute("users", userDetailsService.getAllUsers().stream().sorted((o1, o2) -> Integer.compare(o1.getId(), o2.getId())))
                .addAttribute("search", "")
                .addAttribute("current_user", user);
        return "admin/usersPage";
    }


    @PostMapping("/users")
    public String getSearchUsers(@RequestParam("search") String search, Model model){
        model.addAttribute("users", userDetailsService.findByNameStartingWith(search))
                .addAttribute("search", search);
        return "admin/usersPage";
    }


    @GetMapping("/users/{id}")
    public String showUserInfo(@PathVariable("id") int id, Model model){
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model.addAttribute("user", userDetailsService.findById(id))
                .addAttribute("roles", Roles.values())
                .addAttribute("current_user", user);
        return "admin/userInfoPage";
    }


    @PatchMapping("/users/{id}/newRole")
    public String selectUserRole(@PathVariable("id") int id, @ModelAttribute("user") User user){
        userDetailsService.update(user, id);
        return String.format("redirect:/admin/users/%d", user.getId());
    }

}
enum Roles{
    ROLE_USER,
    ROLE_TEACHER,
    ROLE_ADMIN
}
