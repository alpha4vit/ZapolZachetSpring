package by.gurinovich.ZapolZachetSpring.DTO;

import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.NotNull;

public class SubjectDTO {
    @JsonView(Views.Public.class)
    private int id;

    @JsonView(Views.Public.class)
    @NotNull
    private String title;

    @JsonView(Views.Public.class)
    @NotNull
    private Integer quantOfLabs;

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

    public static SubjectDTO convertToDTO(Subject subject){
        SubjectDTO dto = new SubjectDTO();
        dto.setId(subject.getId());
        dto.setTitle(subject.getTitle());
        dto.setQuantOfLabs(subject.getQuantOfLabs());
        return dto;
    }
}
