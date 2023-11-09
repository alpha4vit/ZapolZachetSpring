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

    @Column(name = "count_of_labas")
    @NotNull
    private Integer countOfLabs;

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

    public Subject(int id, String title, Integer countOfLabs, List<Group> groups, List<Laba> labas) {
        this.id = id;
        this.title = title;
        this.countOfLabs = countOfLabs;
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

    public Integer getCountOfLabs() {
        return countOfLabs;
    }

    public void setCountOfLabs(Integer quantOfLabs) {
        this.countOfLabs = quantOfLabs;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Laba> getLabas() {
        labas.sort((o1, o2) -> {return Integer.compare(o1.getNumber(), o2.getNumber());});
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
