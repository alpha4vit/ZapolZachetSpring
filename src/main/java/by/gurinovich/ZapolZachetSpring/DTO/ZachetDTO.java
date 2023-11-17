package by.gurinovich.ZapolZachetSpring.DTO;

import by.gurinovich.ZapolZachetSpring.models.Laba;
import by.gurinovich.ZapolZachetSpring.models.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ZachetDTO {
    private int id;

    @Pattern(regexp = "[+-]", message = "Значение должно быть в формате \"+\" / \"-\"")
    private String value;
    private LabaDTO laba;

}
