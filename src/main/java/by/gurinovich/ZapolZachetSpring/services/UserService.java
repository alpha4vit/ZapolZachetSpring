package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.User;
import by.gurinovich.ZapolZachetSpring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getAuthenticatedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null) {
            User user = (User) principal;
            return userRepository.findById(user.getId()).orElse(null);
        }
        return null;
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getByName(String name){
        return userRepository.findByUsername(name).orElse(null);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getByNameStartingWith(String name){
        return userRepository.findByUsernameStartingWith(name);
    }

    @Transactional
    public void update(User user, Long id){
        User res = userRepository.findById(id).orElse(null);
        res.setRole(user.getRole());
        userRepository.save(res);
    }

    @Transactional
    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }
}
