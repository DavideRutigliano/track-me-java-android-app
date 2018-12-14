package com.github.ferrantemattarutigliano.software.server.model.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
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
    private Collection<Individual> athletes = new ArrayList(new LinkedHashSet());

    @ManyToMany
    @JoinTable(name = "runWatch",
            joinColumns = {@JoinColumn(name = "runId")},
            inverseJoinColumns = {@JoinColumn(name = "spectatorId")})
    private Collection<Individual> spectators = new ArrayList(new LinkedHashSet());

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

    public Individual getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Individual organizer) {
        this.organizer = organizer;
    }

    public Collection<Individual> getAthletes() {
        return athletes;
    }

    public void enrollAthlete(Individual athlete) {
        this.athletes.add(athlete);
    }

    public void removeAthlete(Individual athlete) {
        this.athletes.remove(athlete);
    }

    public Collection<Individual> getSpectators() {
        return spectators;
    }

    public void addSpectator(Individual spectator) {
        this.spectators.add(spectator);
    }

    public void removeSpectator(Individual spectator) {
        this.athletes.remove(spectator);
    }
}
