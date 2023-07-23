package by.gurinovich.ZapolZachetSpring.DTO;

import org.springframework.stereotype.Component;

@Component
public class Request {
    private Integer group_id;
    private Integer subject_id;

    private String surnameSearch;
    private Integer labaNumFilter;

    private Integer student_id;
    private Integer labaNumZachet;
    private String value;

    private Integer newQuantOfLabas;
    private Integer newLabaNum;
    private String newLabaTitle;


    private String type;

    public Request(Integer group_id, Integer subject_id, String surnameSearch, Integer labaNumFilter, Integer student_id,
                   Integer labaNumZachet, String value, Integer newQuantOfLabas, Integer newLabaNum, String newLabaTitle, String type) {
        this.group_id = group_id;
        this.subject_id = subject_id;
        this.surnameSearch = surnameSearch;
        this.labaNumFilter = labaNumFilter;
        this.student_id = student_id;
        this.labaNumZachet = labaNumZachet;
        this.value = value;
        this.newQuantOfLabas = newQuantOfLabas;
        this.newLabaNum = newLabaNum;
        this.newLabaTitle = newLabaTitle;
        this.type = type;
    }

    public Request() {
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public Integer getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(Integer subject_id) {
        this.subject_id = subject_id;
    }

    public String getSurnameSearch() {
        return surnameSearch;
    }

    public void setSurnameSearch(String surnameSearch) {
        this.surnameSearch = surnameSearch;
    }

    public Integer getLabaNumFilter() {
        return labaNumFilter;
    }

    public void setLabaNumFilter(Integer labaNumFilter) {
        this.labaNumFilter = labaNumFilter;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public Integer getLabaNumZachet() {
        return labaNumZachet;
    }

    public void setLabaNumZachet(Integer labaNumZachet) {
        this.labaNumZachet = labaNumZachet;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getNewQuantOfLabas() {
        return newQuantOfLabas;
    }

    public void setNewQuantOfLabas(Integer newQuantOfLabas) {
        this.newQuantOfLabas = newQuantOfLabas;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNewLabaNum() {
        return newLabaNum;
    }

    public void setNewLabaNum(Integer newLabaNum) {
        this.newLabaNum = newLabaNum;
    }

    public String getNewLabaTitle() {
        return newLabaTitle;
    }

    public void setNewLabaTitle(String newLabaTitle) {
        this.newLabaTitle = newLabaTitle;
    }
}