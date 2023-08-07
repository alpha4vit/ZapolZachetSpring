package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.*;
import by.gurinovich.ZapolZachetSpring.repositories.ZachetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ZachetService {
    private final ZachetRepository zachetRepository;
    private final StudentService studentService;
    private final LabaService labaService;

    @Autowired
    public ZachetService(ZachetRepository zachetRepository, StudentService studentService, LabaService labaService) {
        this.zachetRepository = zachetRepository;
        this.studentService = studentService;
        this.labaService = labaService;
    }

    @Transactional
    public void save(Zachet zachet){
        zachetRepository.save(zachet);
    }

    public List<Zachet> findByStudentAndSubject(Student student, Subject subject){
        List<Zachet> result = new ArrayList<>();
        List<Zachet> temp = zachetRepository.findByStudent(student);
        for (Zachet zachet : temp){
            if (zachet.getLaba().getSubject().equals(subject)) {
                result.add(zachet);
            }
        }
        return result.stream().sorted((z1, z2) -> Integer.compare(z1.getLaba().getNumber(), z2.getLaba().getNumber())).toList();
    }

    @Transactional
    public void update(Zachet zachet){
        Optional<Zachet> temp = zachetRepository.findByStudentAndLaba(studentService.findById(zachet.getStudent().getId()),
                labaService.findById(zachet.getLaba().getId()));
        if (temp.isPresent()){
            zachet.setId(temp.get().getId());
            zachetRepository.save(zachet);
        }
        zachetRepository.save(zachet);
    }

    @Transactional
    public void delete(Zachet zachet){
        zachetRepository.delete(zachet);
    }


}
