package by.gurinovich.ZapolZachetSpring.utils.mappers.impl;

import by.gurinovich.ZapolZachetSpring.DTO.StudentDTO;
import by.gurinovich.ZapolZachetSpring.models.Student;
import by.gurinovich.ZapolZachetSpring.utils.mappers.Mappable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentMapper implements Mappable<Student, StudentDTO> {
    private final ModelMapper modelMapper;

    @Override
    public Student fromDTO(StudentDTO dto) {
        return modelMapper.map(dto, Student.class);
    }

    @Override
    public StudentDTO toDTO(Student entity) {
        return modelMapper.map(entity, StudentDTO.class);
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
