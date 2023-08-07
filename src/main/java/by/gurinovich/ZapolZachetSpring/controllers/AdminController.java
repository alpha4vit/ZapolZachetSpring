package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.DTO.Request;
import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.auth.User;
import by.gurinovich.ZapolZachetSpring.security.UserDetails;
import by.gurinovich.ZapolZachetSpring.services.GroupService;
import by.gurinovich.ZapolZachetSpring.services.LabaService;
import by.gurinovich.ZapolZachetSpring.services.StudentService;
import by.gurinovich.ZapolZachetSpring.services.SubjectService;
import by.gurinovich.ZapolZachetSpring.services.auth.UserDetailsService;
import by.gurinovich.ZapolZachetSpring.utils.validotors.GroupValidator;
import by.gurinovich.ZapolZachetSpring.utils.validotors.StudentValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    private final SubjectService subjectService;
    private final LabaService labaService;


    @Autowired
    public AdminController(GroupService groupService, GroupValidator groupValidator, StudentService studentService, StudentValidator studentValidator, UserDetailsService userDetailsService, SubjectService subjectService, LabaService labaService) {
        this.groupService = groupService;
        this.groupValidator = groupValidator;
        this.studentService = studentService;
        this.studentValidator = studentValidator;
        this.userDetailsService = userDetailsService;
        this.subjectService = subjectService;
        this.labaService = labaService;
    }

    @GetMapping("/groups")
    public String showAdminPage(Model model, @ModelAttribute("group") Group group){
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model
                .addAttribute("groups", groupService.getGroups())
                .addAttribute("current_user", user);
        return "admin/groups";
    }

    @PatchMapping("/groups/deleteGroup/{id}")
    public String deleteGroup(@PathVariable("id") int id){
        groupService.deleteById(id);
        return "redirect:/admin/groups";
}

    @PostMapping("/groups/createNewGroup")
    public String createNewGroup(@ModelAttribute("group") @Valid Group group, BindingResult bindingResult, Model model){
        groupValidator.validate(group, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.getGroups());
            return "groups";
        }
        groupService.save(group);
        return "redirect:/admin/groups";
    }

    @GetMapping("/groups/{id}")
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

    @PostMapping("/groups/{group_id}/addStudent")
    public String addStudent(@PathVariable("group_id") int group_id, @ModelAttribute("newStudent") @Valid Student student, BindingResult bindingResult, Model model){
        studentValidator.validate(student, bindingResult);
        if (bindingResult.hasErrors()){
            model.addAttribute("group", groupService.findById(group_id));
            return "admin/groupInfo";
        }
        studentService.save(student, group_id);
        return String.format("redirect:/admin/groups/%d", group_id);

    }

    @PostMapping("/groups/{group_id}/{student_id}/deleteStudent")
    public String deleteStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id){
        studentService.deleteStudentById(student_id);
        return String.format("redirect:/admin/groups/%d", group_id);
    }

    @GetMapping("/groups/{group_id}/{student_id}/editStudent")
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

    @PatchMapping("/groups/{group_id}/{student_id}/editStudent")
    public String editStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id, @ModelAttribute("student") @Valid Student student, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            return "admin/studentEditPage";
        }
        studentService.update(student, student_id, group_id);
        return String.format("redirect:/admin/groups/%d", group_id);
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

    @GetMapping("/subjects")
    public String getSubjects(Model model, @ModelAttribute("newSubject") Subject subject){
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model.addAttribute("user", user)
                .addAttribute("subjects", subjectService.getSubjects());
        return "admin/subjects";
    }

    @PostMapping("/subjects/createNewSubject")
    public String createNewSubject(Model model, @ModelAttribute("newSubject") @Valid Subject subject, BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            subjectService.save(subject);
            return "redirect:/admin/subjects";
        }
        model.addAttribute("subjects", subjectService.getSubjects());
        return "admin/subjects";
    }

    @PatchMapping("/subjects/deleteSubject/{id}")
    public String deleteSubject(@PathVariable("id") int id) {
        subjectService.deleteById(id);
        return "redirect:/admin/subjects";
    }

    @GetMapping("/subjects/{subject_id}")
    public String getSubjectInfo(Model model, @PathVariable("subject_id") Integer subject_id,
                                 @ModelAttribute("tempSubject") Subject subject){
        model.addAttribute("subject", subjectService.findById(subject_id)) ;

        return "admin/subjectInfo";
    }


    @ResponseBody
    @PostMapping("/subjects/{subject_id}/editQuantOfLabas")
    public ResponseEntity<Object> editQuantOfLabas(@RequestBody Request request, @PathVariable("subject_id") Integer subject_id){
        boolean updated = subjectService.updateQuantOfLabas(subjectService.findById(subject_id), request.getNewQuantOfLabas());
        if (updated)
            return new ResponseEntity<>(request.getNewQuantOfLabas(), HttpStatusCode.valueOf(200));
        return new ResponseEntity<>(HttpStatusCode.valueOf(404));
    }


    @PostMapping("/subjects/{subject_id}/selectType")
    public String selectTypeOfPage(Model model, @RequestBody Request request, @PathVariable("subject_id") Integer subject_id){
        Subject subject = subjectService.findById(subject_id);
        model
                .addAttribute("subject", subject);
        if (request.getType().equals("groups")) {
            model
                    .addAttribute("groups", subject.getGroups())
                    .addAttribute("availableGroups", subjectService.getAvailableGroups(subject));
            return "admin/subjectsGroups";
        }
        else {

            return "admin/subjectsLabas";
        }
    }

    @ResponseBody
    @PostMapping("/subjects/{subject_id}/addGroup")
    public ResponseEntity<Object> addGroupToSubject(@PathVariable("subject_id") Integer subject_id, @RequestBody Request request){
        subjectService.addNewGroup(subject_id, request.getGroup_id());
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @ResponseBody
    @PostMapping("/subjects/{subject_id}/deleteGroup/{group_id}")
    public ResponseEntity<Object> deleteGroupFromSubject(@PathVariable("subject_id") Integer subject_id,
                                                         @PathVariable("group_id") Integer group_id){
        if (subjectService.deleteGroupFromSubject(subject_id, group_id))
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        return new ResponseEntity<>("Ошибка удаления", HttpStatusCode.valueOf(404));
    }

    @ResponseBody
    @PostMapping("/subjects/{subject_id}/createLaba")
    public ResponseEntity<Object> createNewLaba(@PathVariable("subject_id") Integer subject_id, @RequestBody Request request){
        if (subjectService.createNewLabaForSubject(subject_id, request.getNewLabaNum(), request.getNewLabaTitle())){
            Integer newQuantOfLabas = subjectService.findById(subject_id).getQuantOfLabs();
            return new ResponseEntity<>(newQuantOfLabas, HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(404));
    }

    @PostMapping("/subjects/{subject_id}/deleteLaba")
    public String deleteLabaForSubject(@RequestBody Request request, Model model, @PathVariable("subject_id") Integer subject_id){
        labaService.deleteById(request.getLaba_id());
        model.addAttribute("subject", subjectService.findById(subject_id));
        return "admin/subjectsLabas";
    }


}
enum Roles{
    ROLE_USER,
    ROLE_TEACHER,
    ROLE_ADMIN
}
