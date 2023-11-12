package by.gurinovich.ZapolZachetSpring.DTO;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Zachet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class StudentDTO {

    private Long id;

    @NotEmpty
    @NotNull
    @Size(min = 2, max = 50, message = "ФИО должно быть от 2 до 50 символов")
    private String fio;

    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private GroupDTO group;

    private List<ZachetDTO> zachety;
}
