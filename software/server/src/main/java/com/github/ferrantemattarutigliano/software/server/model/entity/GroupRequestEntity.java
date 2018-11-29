package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class GroupRequestEntity extends RequestEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String criteria;

    protected GroupRequestEntity() {}

    public GroupRequestEntity(String criteria) {
        this.criteria = criteria;
    }
}
