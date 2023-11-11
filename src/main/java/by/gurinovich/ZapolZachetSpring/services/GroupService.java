package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.DTO.GroupDTO;
import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.repositories.GroupRepository;
import by.gurinovich.ZapolZachetSpring.repositories.StudentRepository;
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
    private final StudentRepository studentRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public void update(Group group, Integer groupId){
        group.setId(groupId);
        groupRepository.save(group);
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
        studentRepository.deleteAllByGroup(groupRepository.findById(id).orElse(null));
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
        return groupRepository.findByName(name).orElse(null);
    }
}
