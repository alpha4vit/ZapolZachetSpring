package by.gurinovich.ZapolZachetSpring.utils.mappers.impl;

import by.gurinovich.ZapolZachetSpring.DTO.SubjectDTO;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.utils.mappers.Mappable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubjectMapper implements Mappable<Subject, SubjectDTO> {
    private final ModelMapper modelMapper;

    @Override
    public Subject fromDTO(SubjectDTO dto) {
        return modelMapper.map(dto, Subject.class);
    }

    @Override
    public SubjectDTO toDTO(Subject entity) {
        return modelMapper.map(entity, SubjectDTO.class);
    }

    @Override
    public List<Subject> fromDTOs(List<SubjectDTO> dtos) {
        return dtos.stream().map(this::fromDTO).toList();
    }

    @Override
    public List<SubjectDTO> toDTOs(List<Subject> entities) {
        return entities.stream().map(this::toDTO).toList();
    }
}
