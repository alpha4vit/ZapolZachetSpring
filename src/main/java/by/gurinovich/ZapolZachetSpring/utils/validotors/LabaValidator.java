package by.gurinovich.ZapolZachetSpring.utils.validotors;


import by.gurinovich.ZapolZachetSpring.models.Laba;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class LabaValidator {

    public static boolean validateLaba(Laba laba){
        List<Laba> labas = laba.getSubject().getLabas();
        for (Laba lb :  labas){
            if (lb.getNumber().intValue() == laba.getNumber().intValue())
                return false;
        }
        return true;

    }
}
