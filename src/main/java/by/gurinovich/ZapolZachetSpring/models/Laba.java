package by.gurinovich.ZapolZachetSpring.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "labas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Laba {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private Subject subject;

    @OneToMany(mappedBy = "laba")
    private List<Zachet> zachets;

    @Override
    public String toString() {
        return "Laba{" +
                "id=" + id +
                ", number=" + number +
                ", title='" + title + '\'' +
                ", subject=" + subject +
                '}';
    }
}
