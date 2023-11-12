package by.gurinovich.ZapolZachetSpring.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Size(min = 1, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 2, message = "Пароль должен содержать от 2 символов")
    private String password;

    @Email(message = "Введенная почта некорректная")
    private String email;

    private String role;
}
