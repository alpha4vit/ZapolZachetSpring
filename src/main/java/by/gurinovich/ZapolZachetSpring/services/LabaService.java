package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.Laba;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.repositories.LabaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LabaService {
    private final LabaRepository labaRepository;

    @Autowired
    public LabaService(LabaRepository labaRepository) {
        this.labaRepository = labaRepository;
    }

    public Laba findById(Integer id){
        return labaRepository.findById(id).orElse(null);
    }

    public Laba findByNumberAndSubject(Integer number, Subject subject){
        return labaRepository.findByNumberAndSubject(number, subject).orElse(null);
    }

    public Laba save(Integer number, String title, Subject subject){
        Optional<Laba> temp = labaRepository.findByNumberAndSubject(number, subject);
        return temp.orElseGet(() -> labaRepository.save(new Laba(number, title, subject)));
    }



}
