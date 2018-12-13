package com.github.ferrantemattarutigliano.software.server.model.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class HealthDataDTO {

    private long id;

    @NotNull
    private String name;

    @NotNull
    private String value;

    @NotNull
    private Date timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
