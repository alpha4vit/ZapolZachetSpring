package by.gurinovich.ZapolZachetSpring.utils.mappers.impl;

import by.gurinovich.ZapolZachetSpring.DTO.LabaDTO;
import by.gurinovich.ZapolZachetSpring.models.Laba;
import by.gurinovich.ZapolZachetSpring.utils.mappers.Mappable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LabaMapper implements Mappable<Laba, LabaDTO> {
    private final ModelMapper modelMapper;

    @Override
    public Laba fromDTO(LabaDTO dto) {
        return modelMapper.map(dto, Laba.class);
    }

    @Override
    public LabaDTO toDTO(Laba entity) {
        return modelMapper.map(entity, LabaDTO.class);
    }

    @Override
    public List<Laba> fromDTOs(List<LabaDTO> dtos) {
        return dtos.stream().map(this::fromDTO).toList();
    }

    @Override
    public List<LabaDTO> toDTOs(List<Laba> entities) {
        return entities.stream().map(this::toDTO).toList();
    }
}
