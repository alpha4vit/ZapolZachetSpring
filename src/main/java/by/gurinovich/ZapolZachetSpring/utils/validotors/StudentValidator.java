package by.gurinovich.ZapolZachetSpring.utils.validotors;

import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class StudentValidator implements Validator {
    private final StudentService studentService;

    @Autowired
    public StudentValidator(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Student.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Student student = (Student) target;
        if (studentService.getByName(student.getFio()) != null)
            errors.rejectValue("fio", "", "Студент с таким именем уже существует");
    }
}
