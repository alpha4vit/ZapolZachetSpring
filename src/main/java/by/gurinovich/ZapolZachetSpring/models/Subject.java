package by.gurinovich.ZapolZachetSpring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "quantoflabs")
    @NotNull
    private Integer quantOfLabs;

    public Subject() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getQuantOfLabs() {
        return quantOfLabs;
    }

    public void setQuantOfLabs(Integer quantOfLabs) {
        this.quantOfLabs = quantOfLabs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return id == subject.id && Objects.equals(title, subject.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
