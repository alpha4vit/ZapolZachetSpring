package by.gurinovich.ZapolZachetSpring.models;


import jakarta.validation.constraints.Pattern;

public class ZachetModel {

    private Student student;

    private Subject subject;

    @Pattern(regexp = "[+-]", message = "Значение должно быть в формате \"+\" / \"-\"")
    private String value;

    private Integer laba_id;

    public ZachetModel() {
    }

    public ZachetModel(Student student, Integer laba_id) {
        this.student = student;
        this.laba_id = laba_id;
    }

    public ZachetModel(Student student) {
        this.student = student;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLaba_id() {
        return laba_id;
    }

    public void setLaba_id(int laba_id) {
        this.laba_id = laba_id;
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
