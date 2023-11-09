package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.*;
import by.gurinovich.ZapolZachetSpring.repositories.LabaRepository;
import by.gurinovich.ZapolZachetSpring.repositories.SubjectRepository;
import by.gurinovich.ZapolZachetSpring.repositories.ZachetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    public List<Subject> getSubjects(){
        return subjectRepository.findAll();
    }

    public Subject findById(int id){
        return subjectRepository.findById(id).orElse(null);
    }

    @Transactional
    public Subject save(Subject subject){
        return subjectRepository.save(subject);
    }

    @Transactional
    public void deleteById(Integer id){
        subjectRepository.deleteById(id);
    }

    @Transactional
    public boolean updateQuantOfLabas(Subject subject, Integer newQuant){
        if (newQuant <= 0)
            return false;
        subject.setCountOfLabs(newQuant);
        subjectRepository.save(subject);
        return true;
    }

    public List<Group> getAvailableGroups(Subject subject){
        List<Group> available = new ArrayList<>();
        for (Group el : groupService.getGroups()){
            if (!subject.getGroups().contains(el)){
                available.add(el);
            }
        }
        return available;
    }


    @Transactional
    public void addNewGroup(Integer subject_id, Integer group_id){
        Group group = groupService.findById(group_id);
        Subject subject = subjectRepository.findById(subject_id).orElse(null);
        List<Group> groups = subject.getGroups();
        groups.add(group);
        subject.setGroups(groups);
        subjectRepository.save(subject);
        createZachetsForNewGroup(subject, group);
    }

    @Transactional
    public boolean deleteGroupFromSubject(Integer subject_id, Integer group_id){
        Subject subject = subjectRepository.findById(subject_id).orElse(null);
        Group group = groupService.findById(group_id);
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
    public boolean createNewLabaForSubject(Integer subject_id, Integer labaNum, String title){

        Subject subject = subjectRepository.findById(subject_id).orElse(null);
        Integer prevQuant = subject.getCountOfLabs();
        subject.setCountOfLabs(prevQuant+1);
        subjectRepository.save(subject);

        Laba laba = new Laba(labaNum, title, subject);
        if (!validateLaba(laba))
            return false;
        labaRepository.save(new Laba(labaNum, title, subject));
        Laba created = labaRepository.findByNumberAndSubject(labaNum, subject).orElse(null);
        updateZachetsForLaba(created.getId());
        return true;
    }

    @Transactional
    public void updateZachetsForLaba(Integer laba_id){
        Laba laba = labaRepository.findById(laba_id).orElse(null);
        for (Group group : laba.getSubject().getGroups()){
            for (Student student : group.getStudents()){
                zachetRepository.save(new Zachet(student, "-", laba));
            }
        }
    }

    @Transactional
    public void createZachetsForNewGroup(Subject subject, Group group){
        for (Student student : group.getStudents()){
            for (Laba laba : subject.getLabas()){
                zachetRepository.save(new Zachet(student, "-", laba));
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

}
