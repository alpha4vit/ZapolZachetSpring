package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.*;
import by.gurinovich.ZapolZachetSpring.repositories.LabaRepository;
import by.gurinovich.ZapolZachetSpring.repositories.SubjectRepository;
import by.gurinovich.ZapolZachetSpring.repositories.ZachetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static by.gurinovich.ZapolZachetSpring.utils.validotors.LabaValidator.validateLaba;

@Service
@Transactional(readOnly = true)
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final GroupService groupService;
    private final LabaRepository labaRepository;
    private final ZachetRepository zachetRepository;
    @Autowired
    public SubjectService(SubjectRepository subjectRepository, GroupService groupService, LabaRepository labaRepository, ZachetRepository zachetRepository) {
        this.subjectRepository = subjectRepository;
        this.groupService = groupService;
        this.labaRepository = labaRepository;
        this.zachetRepository = zachetRepository;
    }

    public List<Subject> getAll(){
        return subjectRepository.findAll();
    }

    public Subject getById(Long id){
        return subjectRepository.findById(id).orElse(null);
    }

    @Transactional
    public Subject save(Subject subject){
        return subjectRepository.save(subject);
    }

    @Transactional
    public void deleteById(Long id){
        subjectRepository.deleteById(id);
    }


    public List<Group> getAvailableGroups(Subject subject){
        List<Group> available = new ArrayList<>();
        for (Group el : groupService.getAll()){
            if (!subject.getGroups().contains(el)){
                available.add(el);
            }
        }
        return available;
    }


    @Transactional
    public void addNewGroup(Long subjectId, Long groupId){
        Group group = groupService.getById(groupId);
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        List<Group> groups = subject.getGroups();
        groups.add(group);
        subject.setGroups(groups);
        subjectRepository.save(subject);
        createZachetsForNewGroup(subject, group);
    }

    @Transactional
    public boolean deleteGroupFromSubject(Long subjectId, Long groupId){
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        Group group = groupService.getById(groupId);
        if (!subject.getGroups().contains(group))
            return false;
        List<Group> groups = subject.getGroups();
        groups.remove(group);
        subject.setGroups(groups);
        subjectRepository.save(subject);
        deleteZachetsWhenDeletingGroup(group, subject);
        return true;
    }

    @Transactional
    public boolean createNewLabaForSubject(Long subjectId, Integer labaNum, String title){

        Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
        if (subjectOptional.isPresent()) {
            Subject subject = subjectOptional.get();
            subjectRepository.save(subject);

            Laba laba = Laba.builder()
                    .subject(subject)
                    .number(labaNum)
                    .title(title)
                    .build();
            if (!validateLaba(laba))
                return false;
            labaRepository.save(laba);
            Laba created = labaRepository.findByNumberAndSubject(labaNum, subject).orElse(null);
            updateZachetsForLaba(created.getId());
            return true;
        }
        return false; //TODO
    }

    @Transactional
    public void updateZachetsForLaba(Long labaId){
        Laba laba = labaRepository.findById(labaId).orElse(null);
        for (Group group : laba.getSubject().getGroups()){
            for (Student student : group.getStudents()){
                zachetRepository.save(
                        Zachet.builder()
                                .laba(laba)
                                .value("-")
                                .student(student)
                                .build()
                );
            }
        }
    }

    @Transactional
    public void createZachetsForNewGroup(Subject subject, Group group){
        for (Student student : group.getStudents()){
            for (Laba laba : subject.getLabas()){
                zachetRepository.save(
                        Zachet.builder()
                                .laba(laba)
                                .value("-")
                                .student(student)
                                .build()
                );
            }
        }
    }

    @Transactional
    public void deleteZachetsWhenDeletingGroup(Group group, Subject subject){
        for (Student student:group.getStudents()) {
            for (Zachet zachet : zachetRepository.findByStudent(student)) {
                if (zachet.getLaba().getSubject().equals(subject)) {
                    zachetRepository.delete(zachet);
                }
            }
        }
    }

    public List<Subject> getAvailableSubjects(Group group) {
        HashSet<Subject> groupSubjects = new HashSet<>(group.getSubjects());
        return getAll().stream().filter(el -> !groupSubjects.contains(el)).toList();
    }
}
