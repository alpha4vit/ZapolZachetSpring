package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.services.GroupService;
import by.gurinovich.ZapolZachetSpring.services.StudentService;
import by.gurinovich.ZapolZachetSpring.utils.validotors.GroupValidator;
import by.gurinovich.ZapolZachetSpring.utils.validotors.StudentValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminWorkWithGroups {
    private final GroupService groupService;
    private final GroupValidator groupValidator;
    private final StudentService studentService;
    private final StudentValidator studentValidator;

    @Autowired
    public AdminWorkWithGroups(GroupService groupService, GroupValidator groupValidator, StudentService studentService, StudentValidator studentValidator) {
        this.groupService = groupService;
        this.groupValidator = groupValidator;
        this.studentService = studentService;
        this.studentValidator = studentValidator;
    }

    @GetMapping()
    public String showAdminPage(Model model, @ModelAttribute("group") Group group){
        model.addAttribute("groups", groupService.getGroups());
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
        model.addAttribute("group", groupService.findById(id));
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
        model.addAttribute("group_id", group_id)
                .addAttribute("student", studentService.findById(student_id))
                .addAttribute("student_id", student_id);
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

}
