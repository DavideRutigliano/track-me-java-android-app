package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class GroupRequest extends Request implements Serializable {
    private String criteria;

    private Boolean anonymized;

    protected GroupRequest() {}

    public GroupRequest(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return criteria;
    }

    public Boolean isAnonymized() {
        return anonymized;
    }

    public void setAnonymized(Boolean anonymized) {
        this.anonymized = anonymized;
    }
}
