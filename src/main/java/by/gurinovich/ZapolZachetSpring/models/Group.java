package by.gurinovich.ZapolZachetSpring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name of group cant by Empty")
    private String name;

    @OneToMany(mappedBy = "group")
    private List<Student> students;

    public Group() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Student> selectStudentsByFilter(TableFilter filter, Subject subject){
        List<Student> selection = new ArrayList<>();
        if (filter.getLabaNum() != null  && filter.getSurnameSearch() != null && !filter.getSurnameSearch().equals("")){
            for (Student st : this.students){
                List<Zachet> zachets = st.getZachety();
                for (int i =0; i < zachets.size(); ++i) {
                    if (zachets.get(i).getStudent().getFio().startsWith(filter.getSurnameSearch()) && zachets.get(i).getValue().equalsIgnoreCase("-") && filter.getLabaNum() == zachets.get(i).getNumber() && zachets.get(i).getSubject().equals(subject)) {
                        selection.add(st);
                        break;
                    }
                }
            }
        }
        else if (filter.getSurnameSearch() != null && !filter.getSurnameSearch().equals("")) {
            for (Student st : this.students) {
                if (st.getFio().startsWith(filter.getSurnameSearch()))
                    selection.add(st);
            }
        }
        else if (filter.getLabaNum() != null){
            for (Student st : this.students){
                List<Zachet> zachets = st.getZachety();
                for (int i =0; i < zachets.size(); ++i) {
                    if (zachets.get(i).getValue().equalsIgnoreCase("-") && filter.getLabaNum() == zachets.get(i).getNumber() && zachets.get(i).getSubject().equals(subject)) {
                        selection.add(st);
                        break;
                    }
                }
            }
        }
        else
            return null;
        return selection;
    }


}
