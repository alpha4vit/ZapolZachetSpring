package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.repositories.GroupRepository;
import by.gurinovich.ZapolZachetSpring.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
    public void update(Group group, Long groupId){
        group.setId(groupId);
        groupRepository.save(group);
    }

    public List<Group> getAll(){
        return groupRepository.findAll();
    }


    @Transactional
    public void deleteById(Long id) {
        studentRepository.deleteAllByGroup(groupRepository.findById(id).orElse(null));
        groupRepository.deleteById(id);
    }

    @Transactional
    public void save(Group group){
        groupRepository.save(group);
    }

    public Group getById(Long id){
        return groupRepository.findById(id).orElse(null);
    }

    public Group getByName(String name){
        return groupRepository.findByName(name).orElse(null);
    }

    public List<Group> getAllBySubjectsNotContains(Subject subject){
        return groupRepository.findAllBySubjectsNotContains(subject);
    }
}
