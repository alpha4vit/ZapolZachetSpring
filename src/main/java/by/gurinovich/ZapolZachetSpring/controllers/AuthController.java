package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.models.auth.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("")
    public String login(@ModelAttribute("user") User user){
        return "auth_v2/authorization";
    }

    @GetMapping("/login/error")
    public String loginError(@ModelAttribute("user") User user, Model model){
        model.addAttribute("error", "Username or password is incorrect!");
        return "auth_v2/authorization";
    }

}
