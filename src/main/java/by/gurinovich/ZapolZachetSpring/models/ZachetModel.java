package by.gurinovich.ZapolZachetSpring.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class ZachetModel {

    private Student student;

    private Subject subject;

    @Pattern(regexp = "[+-]", message = "Значение должно быть в формате \"+\" / \"-\"")
    private String value;

    @Min(value = 1, message = "Номер лабораторной должен быть больше 0")
    private int number;

    public ZachetModel() {
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
}
