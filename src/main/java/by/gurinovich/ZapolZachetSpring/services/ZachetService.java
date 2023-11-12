package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.*;
import by.gurinovich.ZapolZachetSpring.repositories.ZachetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ZachetService {
    private final ZachetRepository zachetRepository;
    private final StudentService studentService;
    private final LabaService labaService;

    @Transactional
    public void save(Zachet zachet){
        zachetRepository.save(zachet);
    }

    public List<Zachet> getByStudentAndSubject(Student student, Subject subject){
        List<Zachet> result = new ArrayList<>();
        List<Zachet> temp = zachetRepository.findByStudent(student);
        for (Zachet zachet : temp){
            if (zachet.getLaba().getSubject().equals(subject)) {
                result.add(zachet);
            }
        }
        return result.stream().sorted(Comparator.comparingInt(z -> z.getLaba().getNumber())).toList();
    }

    @Transactional
    public void update(Zachet zachet){
        Optional<Zachet> temp = zachetRepository.findByStudentAndLaba(studentService.getById(zachet.getStudent().getId()),
                labaService.getById(zachet.getLaba().getId()));
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

    @Transactional
    public void deleteById(Long zachetId){
        zachetRepository.deleteById(zachetId);
    }

}
