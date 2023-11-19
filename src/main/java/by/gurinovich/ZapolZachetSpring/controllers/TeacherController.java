package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.DTO.GroupAndSubject;
import by.gurinovich.ZapolZachetSpring.DTO.Request;
import by.gurinovich.ZapolZachetSpring.models.*;
import by.gurinovich.ZapolZachetSpring.services.*;
import by.gurinovich.ZapolZachetSpring.services.senders.impls.ExcelSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final ZachetService zachetService;
    private final LabaService labaService;
    private final UserService userService;
    private final ExcelSender excelSender;


    @GetMapping()
    public String chooseGroupPage(Model model,
                                  @ModelAttribute("groupANDsubject") GroupAndSubject groupAndSubject,
                                  @ModelAttribute("zachetModel") ZachetModel zachetModel,
                                  @ModelAttribute("request") Request request) {
        User user = userService.getAuthenticatedUser();
        model.addAttribute("user", user)
                .addAttribute("groups", groupService.getAll())
                .addAttribute("current_user", user);
        return "teachers/v2/choosePage";
    }

    @PostMapping("/zachets/new")
    public String newZachet(@RequestBody Request request, BindingResult bindingResult, Model model){
        Group group = groupService.getById(request.getGroupId());
        Subject subject = subjectService.getById(request.getSubjectId());
        Student student = studentService.getById(request.getStudentId());
        zachetService.update(Zachet.builder()
                        .student(student)
                        .value(request.getValue())
                        .laba(labaService.getById(request.getNewZachetLabaId()))
                .build());
        studentService.updatePerfomance(student);
        List<Student> students = group.selectStudentsByFilter(request.getSurnameSearch(), request.getLabaNumFilter(), subject);
        if (students == null)
            students = group.getStudents();
        model.addAttribute("groupANDsubject", new GroupAndSubject(group, subject))
                .addAttribute("students", students)
                .addAttribute("zachetService", zachetService)
                .addAttribute("request", request);
        return "users/v2/table";
    }

    @PostMapping("/group/select")
    public String selectUser(Model model, @RequestBody Request request){
        Group group = groupService.getById(request.getGroupId());
        Subject subject = subjectService.getById(request.getSubjectId());
        List<Student> students = group.selectStudentsByFilter(request.getSurnameSearch(), request.getLabaNumFilter(), subject);
        if (students == null)
            students = group.getStudents();
        model.addAttribute("groupANDsubject", new GroupAndSubject(group, subject))
                .addAttribute("students", students)
                .addAttribute("zachetService", zachetService)
                .addAttribute("zachetModel", new ZachetModel())
                .addAttribute("request", request);
        return "users/v2/table";
    }

    @PostMapping("/group/send/excel")
    @ResponseBody
    public void sendExcelToEmail(@RequestBody Request request){
        if (request.getSubjectId() == null && request.getGroupId() != null){
            excelSender.send(groupService.getById(request.getGroupId()));
        }
        else if (request.getGroupId() != null && request.getSubjectId() != null){
            excelSender.send(groupService.getById(request.getGroupId()), subjectService.getById(request.getSubjectId()));
        }
    }

}
