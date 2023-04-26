package by.gurinovich.ZapolZachetSpring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

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

    @OneToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private Subject subject;

    @Column(name = "value")
    @Pattern(regexp = "[+-]", message = "Значение должно быть в формате \"+\" / \"-\"")
    private String value;

    @Column(name = "number")
    @Min(value = 1, message = "Номер лабораторной должен быть больше 0")
    private int number;

    public Zachet() {
    }

    public Zachet(Student student, Subject subject, String value, int number) {
        this.student = student;
        this.subject = subject;
        this.value = value;
        this.number = number;
    }



    public Zachet(Subject subject) {
        this.subject = subject;
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

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
