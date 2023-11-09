package by.gurinovich.ZapolZachetSpring.utils.validotors;


import by.gurinovich.ZapolZachetSpring.models.Zachet;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ZachetValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Zachet.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Zachet zachet = (Zachet) target;
        int number = zachet.getLaba().getSubject().getCountOfLabs();
        if (zachet.getLaba().getNumber() > number)
            errors.rejectValue("number", "", "Лабораторной работы с данным номером не существует");
    }
}
