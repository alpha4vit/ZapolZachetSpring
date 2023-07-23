package by.gurinovich.ZapolZachetSpring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
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

    @ManyToMany
    @JoinTable(
            name = "group_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<Group> groups;

    @OneToMany(mappedBy = "subject")
    private List<Laba> labas;

    public Subject() {}

    public Subject(int id, String title, Integer quantOfLabs, List<Group> groups, List<Laba> labas) {
        this.id = id;
        this.title = title;
        this.quantOfLabs = quantOfLabs;
        this.groups = groups;
        this.labas = labas;
    }

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

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Laba> getLabas() {
        return labas;
    }

    public void setLabas(List<Laba> labas) {
        this.labas = labas;
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
