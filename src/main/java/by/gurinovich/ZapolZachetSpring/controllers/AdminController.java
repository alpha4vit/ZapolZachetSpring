package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.DTO.Request;
import by.gurinovich.ZapolZachetSpring.DTO.StudentDTO;
import by.gurinovich.ZapolZachetSpring.DTO.SubjectDTO;
import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.models.User;
import by.gurinovich.ZapolZachetSpring.services.*;
import by.gurinovich.ZapolZachetSpring.utils.enums.Roles;
import by.gurinovich.ZapolZachetSpring.utils.mappers.impl.StudentMapper;
import by.gurinovich.ZapolZachetSpring.utils.mappers.impl.SubjectMapper;
import by.gurinovich.ZapolZachetSpring.utils.validotors.GroupValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final GroupService groupService;
    private final GroupValidator groupValidator;
    private final StudentService studentService;
    private final UserService userService;
    private final SubjectService subjectService;
    private final LabaService labaService;
    private final StudentMapper studentMapper;
    private final SubjectMapper subjectMapper;

    @GetMapping("/groups")
    public String showAdminPage(Model model, @ModelAttribute("group") Group group){
        User user = userService.getAuthenticatedUser();
        model
                .addAttribute("groups", groupService.getAll())
                .addAttribute("current_user", user);
        return "admin/v2/groups";
    }


    @PostMapping("/groups/new")
    public String createNewGroup(@ModelAttribute("group") @Valid Group group, BindingResult bindingResult, Model model){
        groupValidator.validate(group, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.getAll());
            return "groups";
        }
        groupService.save(group);
        return "redirect:/admin/groups";
    }


    @GetMapping("/groups/{id}/subjects")
    public String getInfoAboutGroup(@PathVariable("id") Long id,
                                    Model model,
                                    @ModelAttribute("newSubject") SubjectDTO subject){
        User user = userService.getAuthenticatedUser();
        model
                .addAttribute("group", groupService.getById(id))
                .addAttribute("current_user", user);
        return "/admin/v2/groupInfoSubjects";
    }

    @GetMapping("/groups/{id}/edit")
    public String editGroupPage(@PathVariable("id") Long id, Model model){
        model
                .addAttribute("group_id", id)
                .addAttribute("group", groupService.getById(id));
        return "admin/v2/groupEditPage";
    }

    @PatchMapping("/groups/{group_id}/edit")
    public String editGroup(@PathVariable("group_id") Long groupId,
                            @ModelAttribute("student") @Valid Group group,
                            BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "admin/v2/studentEditPage";
        }
        groupService.update(group, groupId);
        return "redirect:/admin/groups";
    }

    @DeleteMapping("/groups/{id}/delete")
    public String deleteGroup(@PathVariable("id") Long id) {
        groupService.deleteById(id);
        return "redirect:/admin/groups";
    }

    @GetMapping("/groups/{id}/students")
    public String getInfoAboutGroupStudents(@PathVariable("id") Long id,
                                            Model model,
                                            @ModelAttribute("newStudent") StudentDTO student){
        User user = userService.getAuthenticatedUser();
        model.addAttribute("group", groupService.getById(id))
                .addAttribute("current_user", user);
        return "/admin/v2/groupInfoStudents";
    }

    @PostMapping("/groups/{group_id}/students/add")
    public String addStudent(@PathVariable("group_id") Long groupId,
                             @ModelAttribute("newStudent") StudentDTO studentDTO){
        studentService.save(studentMapper.fromDTO(studentDTO), groupId);
        return String.format("redirect:/admin/groups/%d/students", groupId);
    }

    @GetMapping("/groups/{group_id}/students/{student_id}/edit")
    public String editStudentPage(@PathVariable("group_id") int groupId, @PathVariable("student_id") Long studentId, Model model){
        User user = userService.getAuthenticatedUser();
        model.addAttribute("group_id", groupId)
                .addAttribute("student", studentService.getById(studentId))
                .addAttribute("student_id", studentId)
                .addAttribute("current_user", user);
        return "admin/v2/studentEditPage";
    }

    @DeleteMapping("/groups/{group_id}/students/{student_id}/delete")
    public String deleteStudent(@PathVariable("group_id") Long groupId,
                                @PathVariable("student_id") Long studentId) {
        studentService.deleteStudentById(studentId);
        return String.format("redirect:/admin/groups/%d/students", groupId);
    }


    @PatchMapping("/groups/{group_id}/students/{student_id}/edit")
    public String editStudent(@PathVariable("group_id") Long groupId,
                              @PathVariable("student_id") Long studentId,
                              @ModelAttribute("student") @Valid StudentDTO studentDTO,
                              BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "admin/v2/studentEditPage";
        }
        Student student = studentMapper.fromDTO(studentDTO);
        studentService.update(student, studentId, groupId);
        return String.format("redirect:/admin/groups/%d/students", groupId);
    }


    @GetMapping("/users")
    public String getUsers(Model model,
                           @ModelAttribute("userForEdit") User userForEdit,
                           @ModelAttribute("search") String search){
        User user = userService.getAuthenticatedUser();
        model.addAttribute("users", userService.getAll().stream().sorted(Comparator.comparing(User::getUsername)).toList())
                .addAttribute("current_user", user)
                .addAttribute("roles", Roles.values());
        return "admin/v2/usersPage";
    }


    @PostMapping("/users")
    public String getSearchUsers(@ModelAttribute("search") String search,
                                 @ModelAttribute("userForEdit") User userForEdit,
                                 Model model){
        model.addAttribute("users", userService.getByNameStartingWith(search))
                .addAttribute("roles", Roles.values());
        return "admin/v2/usersPage";
    }


    @GetMapping("/users/{id}")
    public String showUserInfo(@PathVariable("id") Long id, Model model){
        User user = userService.getAuthenticatedUser();
        model.addAttribute("user", userService.getById(id))
                .addAttribute("roles", Roles.values())
                .addAttribute("current_user", user);
        return "admin/userInfoPage";
    }


    @PatchMapping("/users/{id}/role")
    public String selectUserRole(@PathVariable("id") Long id,
                                 @ModelAttribute("userForEdit") User user,
                                 @ModelAttribute("search") String search,
                                 Model model){
        userService.update(user, id);
        model.addAttribute("users", userService.getByNameStartingWith(search))
                .addAttribute("roles", Roles.values());
        return "admin/v2/usersPage";
    }

    @GetMapping("/subjects")
    public String getSubjects(Model model, @ModelAttribute("newSubject") SubjectDTO subjectDTO){
        User user = userService.getAuthenticatedUser();
        model.addAttribute("user", user)
                .addAttribute("subjects", subjectService.getAll());
        return "admin/v2/subjects";
    }

    @PostMapping("/subjects/add")
    public String createNewSubject(Model model, @ModelAttribute("newSubject") @Valid SubjectDTO subjectDTO, BindingResult bindingResult){
        if (!bindingResult.hasErrors()){
            subjectService.save(subjectMapper.fromDTO(subjectDTO));
            return "redirect:/admin/subjects";
        }
        model.addAttribute("subjects", subjectService.getAll());
        return "admin/v2/subjects";
    }


    @DeleteMapping("/subjects/{id}/delete")
    public String deleteSubject(@PathVariable("id") Long id) {
        subjectService.deleteById(id);
        return "redirect:/admin/subjects";
    }

    @GetMapping("/subjects/{id}/edit") //TODO
    public String editSubjectPage(@PathVariable("id") Long id,
                              Model model) {
        model.addAttribute("subject", subjectService.getById(id));
        return "redirect:/admin/subjects";
    }

    @PatchMapping("/subjects/{id}/edit") //TODO
    public String editSubject(@PathVariable("id") Long id,
                              @ModelAttribute("subject_id") SubjectDTO subject) {
        return "redirect:/admin/subjects";
    }

    @GetMapping("/subjects/{subject_id}/groups")
    public String getSubjectGroupsInfo(Model model, @PathVariable("subject_id") Long subjectId,
                                 @ModelAttribute("tempSubject") Subject subject){
        Subject current = subjectService.getById(subjectId);
        model.addAttribute("subject", subjectService.getById(subjectId))
                .addAttribute("availableGroups", subjectService.getAvailableGroups(current));
        return "admin/v2/subjectInfoGroups";
    }

    @GetMapping("/subjects/{subject_id}/labas")
    public String getSubjectLabasInfo(Model model, @PathVariable("subject_id") Long subjectId,
                                 @ModelAttribute("tempSubject") Subject subject){
        model.addAttribute("subject", subjectService.getById(subjectId)) ;
        return "admin/v2/subjectInfoLabas";
    }


    @Deprecated
    @PostMapping("/subjects/{subject_id}/selectType")
    public String selectTypeOfPage(Model model, @RequestBody Request request, @PathVariable("subject_id") Long subjectId){
        Subject subject = subjectService.getById(subjectId);
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
    @PostMapping("/subjects/{subject_id}/groups/add")
    @ResponseStatus(HttpStatus.OK)
    public void addGroupToSubject(@PathVariable("subject_id") Long subjectId,
                                                    @RequestParam("group_id") Long groupId){
        subjectService.addNewGroup(subjectId, groupId);
    }

    @DeleteMapping("/subjects/{subject_id}/groups/{group_id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGroupFromSubject(@PathVariable("subject_id") Long subjectId,
                                                         @PathVariable("group_id") Long groupId){
        subjectService.deleteGroupFromSubject(subjectId, groupId);
    }

    @PostMapping("/subjects/{subject_id}/labas/delete")
    public String deleteLabaForSubject(@RequestBody Request request,
                                       Model model,
                                       @PathVariable("subject_id") Long subject_id){
        labaService.deleteById(request.getLabaId());
        model.addAttribute("subject", subjectService.getById(subject_id));
        return "admin/subjectsLabas";
    }


}

