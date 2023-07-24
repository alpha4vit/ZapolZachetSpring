package by.gurinovich.ZapolZachetSpring.controllers.API;

import by.gurinovich.ZapolZachetSpring.models.auth.User;
import by.gurinovich.ZapolZachetSpring.services.auth.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserAPIController {

    private final UserDetailsService userDetailsService;

    @Autowired
    public UserAPIController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(userDetailsService.getAllUsers(), HttpStatusCode.valueOf(200));
    }

}
