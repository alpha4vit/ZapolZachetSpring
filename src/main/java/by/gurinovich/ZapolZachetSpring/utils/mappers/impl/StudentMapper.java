package by.gurinovich.ZapolZachetSpring.utils.mappers.impl;

import by.gurinovich.ZapolZachetSpring.DTO.StudentDTO;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.utils.mappers.Mappable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentMapper implements Mappable<Student, StudentDTO> {
    private final ModelMapper modelMapper;

    @SneakyThrows
    @Override
    public Student fromDTO(StudentDTO dto) {
        Student student = modelMapper.map(dto, Student.class);
        if (dto.getDateOfBirth()!=null) {
            student.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(dto.getDateOfBirth()));
        }
        return student;
    }

    @Override
    public StudentDTO toDTO(Student entity) {
        StudentDTO dto = modelMapper.map(entity, StudentDTO.class);
        dto.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").format(entity.getDateOfBirth()));
        return dto;
    }

    @Override
    public List<Student> fromDTOs(List<StudentDTO> dtos) {
        return dtos.stream().map(this::fromDTO).toList();
    }

    @Override
    public List<StudentDTO> toDTOs(List<Student> entities) {
        return entities.stream().map(this::toDTO).toList();
    }



}
