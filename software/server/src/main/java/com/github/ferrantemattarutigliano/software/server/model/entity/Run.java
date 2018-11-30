package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
public class Run {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private Date date;
    private Date time;
    private String path;

    @ManyToOne
    @JoinColumn(name = "organizerId", nullable = false)
    private Individual organizer;

    @ManyToMany
    @JoinTable(name = "runEnroll",
            joinColumns = {@JoinColumn(name = "runId")},
            inverseJoinColumns = {@JoinColumn(name = "athleteId")})
    private Set<Individual> athletes;

    @ManyToMany
    @JoinTable(name = "runWatch",
            joinColumns = {@JoinColumn(name = "runId")},
            inverseJoinColumns = {@JoinColumn(name = "spectatorId")})
    private Set<Individual> spectators;

    protected Run() {}

    public Run(String title, Date date, Date time, String path) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public Date getTime() {
        return time;
    }

    public String getPath() {
        return path;
    }

    public Individual getOrganizer() {
        return organizer;
    }

    public Set<Individual> getAthletes() {
        return athletes;
    }

    public Set<Individual> getSpectators() {
        return spectators;
    }
}
