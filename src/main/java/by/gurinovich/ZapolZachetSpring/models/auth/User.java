package by.gurinovich.ZapolZachetSpring.models.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @Size(min = 1, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String name;

    @Column(name = "password")
    @Size(min = 2, message = "Пароль должен содержать от 2 символов")
    private String password;

    @Column(name = "email")
    @Email(message = "Введенная почта некорректная")
    private String email;

    @Column(name = "role")
    private String role;

}
