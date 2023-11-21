package by.gurinovich.ZapolZachetSpring.controllers;

import by.gurinovich.ZapolZachetSpring.DTO.UserDTO;
import by.gurinovich.ZapolZachetSpring.models.User;
import by.gurinovich.ZapolZachetSpring.services.EmailService;
import by.gurinovich.ZapolZachetSpring.services.UserService;
import by.gurinovich.ZapolZachetSpring.utils.mappers.impl.UserMapper;
import by.gurinovich.ZapolZachetSpring.utils.validotors.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserValidator userValidator;
    private final UserService userService;
    private final EmailService emailService;

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
    public String registrationPage(@ModelAttribute("user") @Valid User user,
                                   BindingResult bindingResult,
                                   Model model){
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth_v2/failed_registration";
        }
        model.addAttribute("user", userService.save(user));
        return "auth_v2/code_confirm";
    }

    @PreAuthorize("!@customSecurityExpression.isEmailVerified()")
    @GetMapping("/confirm")
    public String confirmEmailPage(Model model){
        model.addAttribute("user", userService.getAuthenticatedUser());
        return "auth_v2/code_confirm";
    }

    @PostMapping("/confirm/{user_id}")
    public String confirmEmail(@PathVariable("user_id") Long userId,
                               @RequestParam("code") String code,
                               Model model) {
        if (userService.enable(userService.getById(userId), code)) {
            return "redirect:/auth";
        }
        else{
            model.addAttribute("user", userService.getAuthenticatedUser())
                    .addAttribute("conf_error", "Введен неверный код!");
            return "auth_v2/code_confirm";
        }
    }

    //@PreAuthorize("!@customSecurityExpression.isEmailVerified()")
    @GetMapping("/confirm/{user_id}/resend")
    @ResponseBody
    public void resendConfirmationCode(@PathVariable("user_id") Long userId){
        User user = userService.getById(userId);
        userService.updateConfirmationCode(user);
        emailService.sendRegistrationEmailMessage(user, new Properties());
    }


}
