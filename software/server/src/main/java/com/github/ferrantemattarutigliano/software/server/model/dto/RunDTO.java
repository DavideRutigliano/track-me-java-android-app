package com.github.ferrantemattarutigliano.software.server.model.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class RunDTO {
    @NotNull
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Date date;

    @NotNull
    private Date time;

    @NotNull
    private String path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
