package by.gurinovich.ZapolZachetSpring.services.auth;

import by.gurinovich.ZapolZachetSpring.models.auth.User;
import by.gurinovich.ZapolZachetSpring.repositories.auth.UserRepository;
import by.gurinovich.ZapolZachetSpring.security.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    @Autowired
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");
        return new UserDetails(user.get());
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findByName(String name){
        return userRepository.findByName(name).orElse(null);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User findById(int id){
        return userRepository.findById(id).orElse(null);
    }

    public void update(User user, int id){
        User res = userRepository.findById(id).orElse(null);
        res.setRole(user.getRole());
        userRepository.save(res);
    }

    public List<User> findByNameStartingWith(String name){
        return userRepository.findByNameStartingWith(name);
    }
}
