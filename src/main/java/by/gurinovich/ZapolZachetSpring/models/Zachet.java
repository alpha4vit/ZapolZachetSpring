package by.gurinovich.ZapolZachetSpring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Entity
@Table(name = "zachety")
public class Zachet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @Column(name = "value")
    @Pattern(regexp = "[+-]", message = "Значение должно быть в формате \"+\" / \"-\"")
    private String value;

    @ManyToOne
    @JoinColumn(name = "laba_id", referencedColumnName = "id")
    private Laba laba;

    public Zachet() {
    }

    public Zachet(Student student, String value, Laba laba) {
        this.student = student;
        this.value = value;
        this.laba = laba;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Laba getLaba() {
        return laba;
    }

    public void setLaba(Laba laba) {
        this.laba = laba;
    }
}
