package by.gurinovich.ZapolZachetSpring.models;

public class TableFilter {
    private String surnameSearch;
    private Integer labaNum;

    public TableFilter() {
    }

    public TableFilter(String surnameSearch, Integer labaNum) {
        this.surnameSearch = surnameSearch;
        this.labaNum = labaNum;
    }

    public String getSurnameSearch() {
        return surnameSearch;
    }

    public void setSurnameSearch(String surnameSearch) {
        this.surnameSearch = surnameSearch;
    }

    public Integer getLabaNum() {
        return labaNum;
    }

    public void setLabaNum(Integer labaNum) {
        this.labaNum = labaNum;
    }
}
