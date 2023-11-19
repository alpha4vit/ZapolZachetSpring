package by.gurinovich.ZapolZachetSpring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.*;

@Entity
@Table(name = "groups")
@Data
public class Group {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "average_performance")
    private Double averagePerformance;

    @OneToMany(mappedBy = "group")
    private List<Student> students;


    @ManyToMany
    @JoinTable(
            name = "group_subject",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjects;


    public List<Student> getStudents() {
        return students.stream().sorted(Comparator.comparing(Student::getFio)).toList();
    }

    public List<Student> selectStudentsByFilter(String surnameSearch, Integer labaNum, Subject subject){
        List<Student> selection = new ArrayList<>();
        if (labaNum != null  && surnameSearch != null && !surnameSearch.equals("")){
            for (Student st : this.students){
                List<Zachet> zachets = st.getZachety();
                for (int i =0; i < zachets.size(); ++i) {
                    if (st.getFio().toLowerCase().startsWith(surnameSearch.toLowerCase()) && zachets.get(i).getValue().equalsIgnoreCase("-") && labaNum == zachets.get(i).getLaba().getNumber() && zachets.get(i).getLaba().getSubject().equals(subject)) {
                        selection.add(st);
                        break;
                    }
                }
            }
        }
        else if (surnameSearch != null && !surnameSearch.equals("")) {
            for (Student st : this.students) {
                if (st.getFio().toLowerCase().startsWith(surnameSearch.toLowerCase()))
                    selection.add(st);
            }
        }
        else if (labaNum != null){
            for (Student st : this.students){
                List<Zachet> zachets = st.getZachety();
                for (int i =0; i < zachets.size(); ++i) {
                    if (zachets.get(i).getValue().equalsIgnoreCase("-") && labaNum == zachets.get(i).getLaba().getNumber() && zachets.get(i).getLaba().getSubject().equals(subject)) {
                        selection.add(st);
                        break;
                    }
                }
            }
        }
        else
            return null;
        selection.sort((Comparator.comparing(Student::getFio)));
        return selection;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
