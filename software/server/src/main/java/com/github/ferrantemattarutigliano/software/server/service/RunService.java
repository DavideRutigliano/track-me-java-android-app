package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.Run;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.RunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class RunService {

    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private RunRepository runRepository;

    public Collection<Run> showRuns() {
        return runRepository.findAll();
    }

    public Collection<Run> showCreatedRuns() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null || !individualRepository.existsByUser(user)) {
            return null;
        }

        Individual organizer = individualRepository.findByUser(user);

        return runRepository.findByOrganizer(organizer);
    }

    public String createRun(Run run) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null || !individualRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        Individual organizer = individualRepository.findByUser(user);

        run.setOrganizer(organizer);

        runRepository.save(run);

        return "Success";
    }

    public String editRun(Run run) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null || !individualRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        if (!runRepository.findById(run.getId()).isPresent()) {
            return "fail";
        }

        Individual organizer = individualRepository.findByUser(user);

        Run existing = runRepository.findById(run.getId()).get();

        if (!run.getOrganizer().getSsn().equals(organizer.getSsn())) {
            return "not organizer: run away :O";
        }

        if (run.getTitle() != null
                && !run.getTitle().equals(existing.getTitle())) {
            existing.setTitle(run.getTitle());
        }

        if (run.getDate() != null
                && !run.getDate().equals(existing.getDate())) {
            existing.setDate(run.getDate());
        }

        if (run.getTime() != null
                && !run.getTime().equals(existing.getTime())) {
            existing.setTime(run.getTime());
        }

        if (run.getPath() != null
                && !run.getPath().equals(existing.getPath())) {
            existing.setPath(run.getPath());
        }

        runRepository.save(existing);

        return "Success";
    }

    public String deleteRun(Long runId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null || !individualRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        if (!runRepository.findById(runId).isPresent()) {
            return "fail";
        }

        Individual organizer = individualRepository.findByUser(user);

        Run run = runRepository.findById(runId).get();

        if (!run.getOrganizer().getSsn().equals(organizer.getSsn())) {
            return "not organizer: run away :O";
        }

        runRepository.delete(run);

        return "Success";
    }

    public String enrollRun(Long runId) {

        if (!runRepository.findById(runId).isPresent()) {
            return "fail";
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null || !individualRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        Run run = runRepository.findById(runId).get();
        Individual athlete = individualRepository.findByUser(user);

        if (run.getAthletes().stream().anyMatch(individual -> individual.getSsn().equals(athlete.getSsn()))) {
            return "already enrolled";
        }

        run.enrollAthlete(athlete);
        runRepository.save(run);

        return "Success";
    }

    public String unenrollRun(Long runId) {

        if (!runRepository.findById(runId).isPresent()) {
            return "fail";
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null || !individualRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        Run run = runRepository.findById(runId).get();
        Individual athlete = individualRepository.findByUser(user);

        if (!run.getAthletes().stream().anyMatch(individual -> individual.getSsn().equals(athlete.getSsn()))) {
            return "not enrolled";
        }

        run.removeAthlete(athlete);
        runRepository.save(run);

        return "Success";
    }

    public String watchRun(Long runId) {

        if (!runRepository.findById(runId).isPresent()) {
            return "fail";
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null || !individualRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        Run run = runRepository.findById(runId).get();
        Individual spectator = individualRepository.findByUser(user);

        if (run.getSpectators().stream().anyMatch(individual -> individual.getSsn().equals(spectator.getSsn()))) {
            return "already watching";
        }

        run.addSpectator(spectator);
        runRepository.save(run);

        return "Success";
    }

    public String unwatchRun(Long runId) {

        if (!runRepository.findById(runId).isPresent()) {
            return "fail";
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null || !individualRepository.existsByUser(user)) {
            return Message.BAD_REQUEST.toString();
        }

        Run run = runRepository.findById(runId).get();
        Individual spectator = individualRepository.findByUser(user);

        if (!run.getSpectators().stream().anyMatch(individual -> individual.getSsn().equals(spectator.getSsn()))) {
            return "not watcher";
        }

        run.removeAthlete(spectator);
        runRepository.save(run);

        return "Success";
    }
}
