package by.gurinovich.ZapolZachetSpring.utils.validotors;

import by.gurinovich.ZapolZachetSpring.models.auth.User;
import by.gurinovich.ZapolZachetSpring.services.auth.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {
    private final UserDetailsService userDetailsService;

    @Autowired
    public UserValidator(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userDetailsService.findByName(user.getName()) != null)
            errors.rejectValue("name", "", "Пользователь с данным именем уже существует!");
        else if (userDetailsService.findByEmail(user.getEmail()) != null)
            errors.rejectValue("email", "", "Пользователь с данной почтой уже существует!");
    }
}
