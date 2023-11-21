package by.gurinovich.ZapolZachetSpring.services;

import by.gurinovich.ZapolZachetSpring.models.User;
import by.gurinovich.ZapolZachetSpring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public User getAuthenticatedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null && !(principal instanceof String)) {
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
        return userRepository.findAll()
                .stream().sorted(Comparator.comparing(User::getUsername)).toList();
    }

    public User getById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getByNameStartingWith(String name){
        return userRepository.findByUsernameStartingWith(name)
                .stream().sorted(Comparator.comparing(User::getUsername)).toList();
    }

    @Transactional
    public void update(User user, Long id){
        User res = userRepository.findById(id).orElse(null);
        res.setRole(user.getRole());
        userRepository.save(res);
    }

    @Transactional
    public User save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setConfirmationCode(generateCofirmationCode());
        userRepository.save(user);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                emailService.sendRegistrationEmailMessage(user, new Properties());
            }
        });
        th.start();
        User created = userRepository.findByEmail(user.getEmail());
        return created;
    }

    @Transactional
    public boolean enable(User user, String code){
        if (user.getConfirmationCode().equals(code)){
            user.setEmailVerified(true);
            user.setConfirmationCode(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public void updateConfirmationCode(User user){
        user.setConfirmationCode(generateCofirmationCode());
        userRepository.save(user);
    }

    private String generateCofirmationCode(){
        Integer code = (int)(1000+Math.random()*9000);
        return code.toString();
    }
}
