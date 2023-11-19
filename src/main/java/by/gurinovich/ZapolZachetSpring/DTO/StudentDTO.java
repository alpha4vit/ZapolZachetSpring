package by.gurinovich.ZapolZachetSpring.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class StudentDTO {

    private Long id;

    @NotEmpty
    @NotNull
    @Size(min = 2, max = 50, message = "ФИО должно быть от 2 до 50 символов")
    private String fio;

    @JsonProperty(value = "date_of_birth")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String dateOfBirth;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double performance;

    private List<ZachetDTO> zachety;
}
