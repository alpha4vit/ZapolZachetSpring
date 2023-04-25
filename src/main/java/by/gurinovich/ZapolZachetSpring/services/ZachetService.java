package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.repositories.ZachetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZachetService {
    private final ZachetRepository zachetRepository;

    @Autowired
    public ZachetService(ZachetRepository zachetRepository) {
        this.zachetRepository = zachetRepository;
    }
}
