package by.gurinovich.ZapolZachetSpring.DTO;

import by.gurinovich.ZapolZachetSpring.models.Zachet;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class LabaDTO {
    private Long id;

    private Integer number;

    private String title;

    private SubjectDTO subject;

}
