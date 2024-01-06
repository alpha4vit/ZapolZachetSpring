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
import org.springframework.security.core.parameters.P;
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
    public String registration(@ModelAttribute("user") @Valid User user,
                                   BindingResult bindingResult,
                                   Model model){
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth_v2/failed_registration";
        }
        User created = userService.save(user);
        emailService.sendRegistrationEmailMessage(created, new Properties());
        model.addAttribute("user", created);
        return "auth_v2/code_confirm";
    }

    @PreAuthorize("!@customSecurityExpression.isEmailVerified()")
    @GetMapping("/confirm")
    public String confirmEmailPage(Model model){
        model.addAttribute("user", userService.getAuthenticatedUser());
        return "auth_v2/code_confirm";
    }

    @PreAuthorize("!@customSecurityExpression.isEmailVerified()")
    @PostMapping("/confirm/{user_id}")
    public String confirmEmail(@PathVariable("user_id") Long userId,
                               @RequestParam("code") String code,
                               Model model) {
        if (userService.enable(userService.getById(userId), code)) {
            return "redirect:/auth";
        }
        else{
            model.addAttribute("user", userService.getById(userId))
                    .addAttribute("conf_error", "Введен неверный код!");
            return "auth_v2/code_confirm";
        }
    }

    @GetMapping("/confirm/{user_id}/resend")
    @ResponseBody
    public void resendConfirmationCode(@PathVariable("user_id") Long userId){
        User user = userService.getById(userId);
        userService.updateConfirmationCode(user);
        emailService.sendRegistrationEmailMessage(user, new Properties());
    }

    @GetMapping("/password/reset/email")
    public String resetPasswordSendEmailPage(){
        return "auth_v2/emailFormForPasswordReset";
    }

    @PostMapping("/password/reset/email")
    public String resetPasswordSendEmail(@RequestParam("email") String email, Model model) {
        User user = userService.getByEmail(email);
        if (user != null) {
            emailService.sendPasswordResetMessage(user, new Properties());
            model.addAttribute("complete", true);
        } else {
            model.addAttribute("error", true);
        }
        return "auth_v2/emailFormForPasswordResetAfter";
    }

    @GetMapping("/password/reset")
    public String resetPasswordPage(@RequestParam("uuid") String uuid,
                                    Model model){
        model.addAttribute("user", userService.getByUUID(uuid))
                .addAttribute("uuid", uuid);
        return "auth_v2/resetPasswordPage";
    }

    @PostMapping("/password/reset")
    public String resetPassword(@RequestParam("uuid") String uuid,
                                @RequestParam("password") String password,
                                @RequestParam("password_confirmation") String passwordConfirmation, Model model){
        User user = userService.getByUUID(uuid);
        if (user != null && passwordConfirmation.equals(password)){
            userService.updatePassword(user, password);
            return "redirect:/auth";
        }
        model.addAttribute("error", true)
                .addAttribute("uuid", uuid);
        return "auth_v2/resetPasswordPage";
    }

}
