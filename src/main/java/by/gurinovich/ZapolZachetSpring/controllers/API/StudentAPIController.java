package by.gurinovich.ZapolZachetSpring.controllers.API;

import by.gurinovich.ZapolZachetSpring.DTO.StudentDTO;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.services.GroupService;
import by.gurinovich.ZapolZachetSpring.services.StudentService;
import by.gurinovich.ZapolZachetSpring.utils.mappers.impl.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentAPIController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final GroupService groupService;
    @GetMapping("")
    public ResponseEntity<List<Student>> getAll(){
        return new ResponseEntity<>(studentService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/{group_id}")
    public ResponseEntity<List<StudentDTO>> getAllByGroup(@PathVariable("group_id") Long groupId){
        return new ResponseEntity<>(studentMapper.toDTOs(
                groupService.getById(groupId).getStudents()),
                HttpStatus.OK);
    }
}
