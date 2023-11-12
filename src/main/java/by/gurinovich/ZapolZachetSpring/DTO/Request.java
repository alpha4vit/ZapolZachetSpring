package by.gurinovich.ZapolZachetSpring.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Request {
    @JsonProperty(value = "group_id")
    private Long groupId;

    @JsonProperty(value = "subject_id")
    private Long subjectId;

    @JsonProperty(value = "laba_id")
    private Long labaId;

    @JsonProperty(value = "student_id")
    private Long studentId;

    @JsonProperty(value = "new_zachet_laba_id")
    private Long newZachetLabaId;

    @JsonProperty(value = "surname_search")
    private String surnameSearch;

    @JsonProperty(value = "laba_num_filter")
    private Integer labaNumFilter;

    private String value;

    @JsonProperty(value = "new_laba_num")
    private Integer newLabaNum;

    @JsonProperty(value = "new_laba_title")
    private String newLabaTitle;

    private String type;

}