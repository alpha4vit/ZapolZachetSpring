package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.DTO.LabaDTO;
import by.gurinovich.ZapolZachetSpring.DTO.Request;
import by.gurinovich.ZapolZachetSpring.DTO.StudentDTO;
import by.gurinovich.ZapolZachetSpring.DTO.SubjectDTO;
import by.gurinovich.ZapolZachetSpring.models.*;
import by.gurinovich.ZapolZachetSpring.services.*;
import by.gurinovich.ZapolZachetSpring.services.senders.impls.ExcelSender;
import by.gurinovich.ZapolZachetSpring.utils.enums.Roles;
import by.gurinovich.ZapolZachetSpring.utils.mappers.impl.GroupMapper;
import by.gurinovich.ZapolZachetSpring.utils.mappers.impl.LabaMapper;
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
    private final LabaMapper labaMapper;
    private final GroupMapper groupMapper;
    private final ExcelSender excelSender;

    @GetMapping("/groups")
    public String showAdminPage(Model model, @ModelAttribute("group") Group group){
        User user = userService.getAuthenticatedUser();
        model
                .addAttribute("groups", groupMapper.toDTOs(groupService.getAll()))
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
        Group group = groupService.getById(id);
        model
                .addAttribute("group", group)
                .addAttribute("current_user", user)
                .addAttribute("available_subjects", subjectService.getAvailableSubjects(group));
        return "admin/v2/groupInfoSubjects";
    }

    @PostMapping("/groups/{group_id}/subjects/add")
    public String subsribeGroupToSubject(@PathVariable("group_id") Long groupId,
                                    @RequestParam("subject_id") Long subjectId,
                                         Model model){
        subjectService.addNewGroup(subjectId, groupId);
        return String.format("redirect:/admin/groups/%d/subjects", groupId);
    }

    @DeleteMapping("/groups/{group_id}/subjects/{subject_id}/delete")
    public String deleteSubjectFromGroup(@PathVariable("group_id") Long groupId,
                                         @PathVariable("subject_id") Long subjectId){
        subjectService.deleteGroupFromSubject(subjectId, groupId);
        return String.format("redirect:/admin/groups/%d/subjects", groupId);
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
        Group group = groupService.getById(id);
        model.addAttribute("group", group)
                .addAttribute("current_user", user)
                .addAttribute("students", studentMapper.toDTOs(group.getStudents()));
        return "admin/v2/groupInfoStudents";
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
                .addAttribute("student", studentMapper.toDTO(studentService.getById(studentId)))
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


    @PatchMapping("/users/{id}/role")
    public String selectUserRole(@PathVariable("id") Long id,
                                 @ModelAttribute("userForEdit") User user,
                                 @RequestParam("search") String search,
                                 Model model){
        System.out.println(user);
        userService.update(user, id);
        model.addAttribute("users", userService.getByNameStartingWith(search))
                .addAttribute("roles", Roles.values())
                .addAttribute("search", search);
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

    @GetMapping("/subjects/{subject_id}/edit")
    public String editSubjectPage(@PathVariable("subject_id") Long subjectId,
                              Model model) {
        model.addAttribute("subject", subjectMapper.toDTO(subjectService.getById(subjectId)));
        return "admin/v2/subjectEditPage";
    }

    @PatchMapping("/subjects/{subject_id}/edit")
    public String editSubject(@PathVariable("subject_id") Long subjectId,
                              @ModelAttribute("subject") SubjectDTO subjectDTO) {
        Subject subject = subjectMapper.fromDTO(subjectDTO);
        subject.setId(subjectId);
        subjectService.save(subject);
        return "redirect:/admin/subjects";
    }

    @GetMapping("/subjects/{subject_id}/groups")
    public String getSubjectGroupsInfo(Model model, @PathVariable("subject_id") Long subjectId){
        Subject current = subjectService.getById(subjectId);
        model.addAttribute("subject", current)
                .addAttribute("availableGroups", subjectService.getAvailableGroups(current));
        return "admin/v2/subjectInfoGroups";
    }

    @GetMapping("/subjects/{subject_id}/labas")
    public String getSubjectLabasInfo(Model model, @PathVariable("subject_id") Long subjectId,
                                 @ModelAttribute("newLaba") LabaDTO laba){
        model.addAttribute("subject", subjectService.getById(subjectId));
        return "admin/v2/subjectInfoLabas";
    }



    @PostMapping("/subjects/{subject_id}/groups/add")
    public String addGroupToSubject(@PathVariable("subject_id") Long subjectId,
                                                    @RequestParam("group_id") Long groupId){
        subjectService.addNewGroup(subjectId, groupId);
        return String.format("redirect:/admin/subjects/%d/groups", subjectId);
    }

    @DeleteMapping("/subjects/{subject_id}/groups/{group_id}/delete")
    public String deleteGroupFromSubject(@PathVariable("subject_id") Long subjectId,
                                                         @PathVariable("group_id") Long groupId){
        subjectService.deleteGroupFromSubject(subjectId, groupId);
        return String.format("redirect:/admin/subjects/%d/groups", subjectId);
    }

    @DeleteMapping("/subjects/{subject_id}/labas/{laba_id}/delete")
    public String deleteLabaForSubject(@PathVariable("subject_id") Long subjectId,
                                       @PathVariable("laba_id") Long labaId){
        labaService.deleteById(labaId);
        return String.format("redirect:/admin/subjects/%d/labas", subjectId);
    }

    @PostMapping("/subjects/{subject_id}/labas/add")
    public String deleteLabaForSubject(@PathVariable("subject_id") Long subjectId,
                                       @ModelAttribute("newLaba") LabaDTO labaDTO){
        Laba laba = labaMapper.fromDTO(labaDTO);
        laba.setSubject(subjectService.getById(subjectId));
        labaService.save(laba);
        return String.format("redirect:/admin/subjects/%d/labas", subjectId);
    }

    @GetMapping("/subjects/{subject_id}/labas/{laba_id}/edit")
    public String editLabaPage(@PathVariable("subject_id") Long subjectId,
                           @PathVariable("laba_id") Long labaId,
                           Model model){
        model.addAttribute("laba", labaMapper.toDTO(labaService.getById(labaId)))
                .addAttribute("subject_id", subjectId);
        return "admin/v2/labaEditPage";
    }

    @PatchMapping("/subjects/{subject_id}/labas/{laba_id}/edit")
    public String editLaba(@PathVariable("subject_id") Long subjectId,
                           @PathVariable("laba_id") Long labaId,
                           @ModelAttribute("laba") LabaDTO labaDTO){
        Laba laba = labaMapper.fromDTO(labaDTO);
        laba.setId(labaId);
        laba.setSubject(subjectService.getById(subjectId));
        labaService.save(laba);
        return String.format("redirect:/admin/subjects/%d/labas", subjectId);
    }

}

