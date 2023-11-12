package by.gurinovich.ZapolZachetSpring.utils.validotors;

import by.gurinovich.ZapolZachetSpring.models.User;
import by.gurinovich.ZapolZachetSpring.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserService userService;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userService.getByName(user.getUsername()) != null)
            errors.rejectValue("name", "", "Пользователь с данным именем уже существует!");
        else if (userService.getByEmail(user.getEmail()) != null)
            errors.rejectValue("email", "", "Пользователь с данной почтой уже существует!");
    }
}
