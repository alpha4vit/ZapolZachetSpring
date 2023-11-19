package by.gurinovich.ZapolZachetSpring.utils.mappers.impl;

import by.gurinovich.ZapolZachetSpring.DTO.GroupDTO;
import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.utils.mappers.Mappable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GroupMapper implements Mappable<Group, GroupDTO> {
    private final ModelMapper modelMapper;

    @Override
    public Group fromDTO(GroupDTO dto) {
        return modelMapper.map(dto, Group.class);
    }

    @Override
    public GroupDTO toDTO(Group entity) {
        GroupDTO groupDTO = modelMapper.map(entity, GroupDTO.class);
        groupDTO.setAveragePerformance(Math.round(entity.getAveragePerformance()*100)/100d);
        return groupDTO;
    }

    @Override
    public List<Group> fromDTOs(List<GroupDTO> dtos) {
        return dtos.stream().map(this::fromDTO).toList();
    }

    @Override
    public List<GroupDTO> toDTOs(List<Group> entities) {
        return entities.stream().map(this::toDTO).toList();
    }
}
