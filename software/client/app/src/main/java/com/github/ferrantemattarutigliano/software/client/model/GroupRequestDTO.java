package com.github.ferrantemattarutigliano.software.client.model;

public class GroupRequestDTO extends RequestDTO {
    private String criteria;

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    @Override
    public String toString() {
        return "Group\n" + criteria + "\n" + getDate().toString() + " " + getTime().toString();
    }
}
