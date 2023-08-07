package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.Laba;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.repositories.LabaRepository;
import by.gurinovich.ZapolZachetSpring.repositories.ZachetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LabaService {
    private final LabaRepository labaRepository;
    private final ZachetRepository zachetRepository;
    @Autowired
    public LabaService(LabaRepository labaRepository, ZachetRepository zachetRepository) {
        this.labaRepository = labaRepository;
        this.zachetRepository = zachetRepository;
    }

    public Laba findById(Integer id){
        return labaRepository.findById(id).orElse(null);
    }

    public Laba findByNumberAndSubject(Integer number, Subject subject){
        return labaRepository.findByNumberAndSubject(number, subject).orElse(null);
    }

    @Transactional
    public Laba save(Integer number, String title, Subject subject){
        Optional<Laba> temp = labaRepository.findByNumberAndSubject(number, subject);
        return temp.orElseGet(() -> labaRepository.save(new Laba(number, title, subject)));
    }

    @Transactional
    public void deleteById(Integer laba_id){
        Laba laba =  labaRepository.findById(laba_id).orElse(null);
        zachetRepository.deleteAllByLaba(laba);
        labaRepository.delete(laba);
    }

}
