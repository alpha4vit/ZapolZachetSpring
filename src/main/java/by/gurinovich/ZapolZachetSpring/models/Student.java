package by.gurinovich.ZapolZachetSpring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "students")
@Data
public class Student {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fio")
    @NotEmpty
    @NotNull
    @Size(min = 2, max = 50, message = "ФИО должно быть от 2 до 50 символов")
    private String fio;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "performance")
    private Double performance;

    @OneToMany(mappedBy = "student")
    private List<Zachet> zachety;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", fio='" + fio + '\'' +
                ", group=" + group +
                ", dateOfBirth=" + dateOfBirth +
                ", performance=" + performance +
                '}';
    }
}
