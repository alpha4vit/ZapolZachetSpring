package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.DTO.GroupDTO;
import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static by.gurinovich.ZapolZachetSpring.DTO.GroupDTO.convertToDTO;

@Service
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getGroups(){
        return groupRepository.findAll();
    }

    public List<GroupDTO> getGroupsDTO(){
        List<GroupDTO> res = new ArrayList<>();
        List<Group> groups = groupRepository.findAll();
        for (Group gr : groups){
            res.add(convertToDTO(gr));
        }
        return res;
    }

    @Transactional
    public void deleteById(int id) {
        groupRepository.deleteById(id);
    }

    @Transactional
    public void save(Group group){
        groupRepository.save(group);
    }

    public Group findById(int id){
        return groupRepository.findById(id).orElse(null);
    }

    public Group findByName(String name){
        return groupRepository.findByName(name);
    }
}
