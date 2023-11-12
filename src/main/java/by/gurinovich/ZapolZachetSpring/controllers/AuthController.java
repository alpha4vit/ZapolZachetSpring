package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.DTO.UserDTO;
import by.gurinovich.ZapolZachetSpring.models.User;
import by.gurinovich.ZapolZachetSpring.services.UserService;
import by.gurinovich.ZapolZachetSpring.utils.mappers.impl.UserMapper;
import by.gurinovich.ZapolZachetSpring.utils.validotors.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserValidator userValidator;
    private final UserService userService;

    @GetMapping("")
    public String login(@ModelAttribute("user") UserDTO user){
        return "auth_v2/authorization";
    }

    @GetMapping("/login/error")
    public String loginError(@ModelAttribute("user") UserDTO user, Model model){
        model.addAttribute("error", "Username or password is incorrect!");
        return "auth_v2/authorization";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") @Valid User user){
        return "auth_v2/failed_registration";
    }

    @PostMapping("/registration")
    public String registrationPage(@ModelAttribute("user") @Valid User user, BindingResult bindingResult){
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth_v2/failed_registration";
        }
        userService.save(user);
        return "redirect:/auth";
    }

}
