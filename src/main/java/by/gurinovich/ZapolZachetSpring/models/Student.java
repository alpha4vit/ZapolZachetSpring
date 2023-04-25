package by.gurinovich.ZapolZachetSpring.models;

import by.gurinovich.ZapolZachetSpring.services.SubjectService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fio")
    @NotEmpty
    @NotNull
    @Size(min = 2, max = 50, message = "ФИО должно быть от 2 до 50 символов")
    private String fio;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @OneToMany(mappedBy = "student")
    private List<Zachet> zachety;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Zachet> getZachety() {
        return zachety;
    }

    public void setZachety(List<Zachet> zachety) {
        this.zachety = zachety;
    }

}
