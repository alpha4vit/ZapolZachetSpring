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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final GroupService groupService;
    private final GroupValidator groupValidator;
    private final StudentService studentService;
    private final UserDetailsService userDetailsService;
    private final SubjectService subjectService;
    private final LabaService labaService;


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
        return "admin/v2/groups";
    }

    @PatchMapping("/groups/deleteGroup/{id}")
    public String deleteGroup(@PathVariable("id") int id){
        groupService.deleteById(id);
        return "redirect:/admin/groups";
}

    @PostMapping("/groups/new")
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
        return "admin/v2/groupInfo";
    }

    @GetMapping("/groups/{id}/edit")
    public String editGroupPage(@PathVariable("id") int id, Model model){
        model
                .addAttribute("group_id", id)
                .addAttribute("group", groupService.findById(id));
        return "admin/v2/groupEditPage";
    }

    @PatchMapping("/groups/{group_id}/edit")
    public String editGroup(@PathVariable("group_id") Integer groupId,
                            @ModelAttribute("student") @Valid Group group,
                            BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "admin/v2/studentEditPage";
        }
        groupService.update(group, groupId);
        return "redirect:/admin/groups";
    }

    @DeleteMapping("/groups/{id}/delete")
    public String deleteGroup(@PathVariable("id") Integer id) {
        groupService.deleteById(id);
        return "redirect:/admin/groups";
    }


    @GetMapping("/groups/{group_id}/students/{student_id}/edit")
    public String editStudentPage(@PathVariable("group_id") int groupId, @PathVariable("student_id") int studentId, Model model){
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model.addAttribute("group_id", groupId)
                .addAttribute("student", studentService.findById(studentId))
                .addAttribute("student_id", studentId)
                .addAttribute("current_user", user);
        return "admin/v2/studentEditPage";
    }

    @DeleteMapping("/groups/{group_id}/students/{student_id}/delete")
    public String deleteStudent(@PathVariable("group_id") Integer groupId,
                                @PathVariable("student_id") Integer studentId) {
        studentService.deleteStudentById(studentId);
        return String.format("redirect:/admin/groups/%d", groupId);
    }

    @PostMapping("/groups/{group_id}/students/add")
    public String addStudent(@PathVariable("group_id") int group_id,
            @ModelAttribute("newStudent") Student student){
        studentService.save(student, group_id);
        return String.format("redirect:/admin/groups/%d", group_id);
    }

    @PatchMapping("/groups/{group_id}/students/{student_id}/edit")
    public String editStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id, @ModelAttribute("student") @Valid Student student, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "admin/v2/studentEditPage";
        }
        studentService.update(student, student_id, group_id);
        return String.format("redirect:/admin/groups/%d", group_id);
    }


    @GetMapping("/users")
    public String getUsers(Model model,
                           @ModelAttribute("userForEdit") User userForEdit,
                           @ModelAttribute("search") String search){
        UserDetails user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            user = (UserDetails) principal;
        }
        model.addAttribute("users", userDetailsService.getAllUsers().stream().sorted((o1, o2) -> Integer.compare(o1.getId(), o2.getId())).toList())
                .addAttribute("current_user", user)
                .addAttribute("roles", Roles.values());
        return "admin/v2/usersPage";
    }


    @PostMapping("/users")
    public String getSearchUsers(@ModelAttribute("search") String search,
                                 @ModelAttribute("userForEdit") User userForEdit,
                                 Model model){
        model.addAttribute("users", userDetailsService.findByNameStartingWith(search))
                .addAttribute("roles", Roles.values());
        return "admin/v2/usersPage";
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
    public String selectUserRole(@PathVariable("id") int id,
                                 @ModelAttribute("userForEdit") User user,
                                 @ModelAttribute("search") String search,
                                 Model model){
        userDetailsService.update(user, id);
        model.addAttribute("users", userDetailsService.findByNameStartingWith(search))
                .addAttribute("roles", Roles.values());
        return "admin/v2/usersPage";
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


    @DeleteMapping("/subjects/delete/{id}")
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
            Integer newQuantOfLabas = subjectService.findById(subject_id).getCountOfLabs();
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
