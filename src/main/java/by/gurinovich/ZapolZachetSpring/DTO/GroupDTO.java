package by.gurinovich.ZapolZachetSpring.DTO;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.utils.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty(message = "Name of group cant by Empty")
    private String name;

    @JsonProperty(value = "average_performance", access = JsonProperty.Access.READ_ONLY)
    private Double averagePerformance;

    private List<SubjectDTO> subjects;

    private List<StudentDTO> students;

}
