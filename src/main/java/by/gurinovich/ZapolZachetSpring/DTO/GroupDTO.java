package by.gurinovich.ZapolZachetSpring.DTO;

import by.gurinovich.ZapolZachetSpring.models.Group;
import by.gurinovich.ZapolZachetSpring.models.Subject;
import by.gurinovich.ZapolZachetSpring.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GroupDTO {

    @JsonView({Views.Public.class})
    private int id;

    @JsonView({Views.Public.class})
    @NotEmpty(message = "Name of group cant by Empty")
    private String name;

    @JsonView(Views.Internal.class)
    private List<SubjectDTO> subjectsDTO;

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

    public List<SubjectDTO> getSubjectsDTO() {
        return subjectsDTO;
    }

    public void setSubjectsDTO(List<SubjectDTO> subjectsDTO) {
        this.subjectsDTO = subjectsDTO;
    }

    public static GroupDTO convertToDTO(Group group){
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(group.getId());
        groupDTO.setName(group.getName());
        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        for (Subject sb : group.getSubjects()){
            subjectDTOList.add(SubjectDTO.convertToDTO(sb));
        }
        groupDTO.setSubjectsDTO(subjectDTOList);
        return groupDTO;
    }
}
