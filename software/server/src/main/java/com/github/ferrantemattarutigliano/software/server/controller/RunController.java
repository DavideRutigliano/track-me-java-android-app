package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.dto.CollectionDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.DTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.RunDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.Run;
import com.github.ferrantemattarutigliano.software.server.service.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/run")
public class RunController {

    @Autowired
    private RunService runService;

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping(path = "/create")
    public String createRun(@RequestBody @DTO(Run.class) Run run) {
        return runService.createRun(run);
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @GetMapping(path = "/show")
    public @CollectionDTO(RunDTO.class)
    Collection<Run> showRuns() {
        return runService.showRuns();
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @GetMapping(path = "/show/created")
    public @CollectionDTO(RunDTO.class)
    Collection<Run> showCreatedRuns() {
        return runService.showCreatedRuns();
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PutMapping(path = "/edit/{id}")
    public String editRun(@PathVariable("id") String runId,
                          @DTO(RunDTO.class) Run run) {
        return runService.editRun(run);
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @DeleteMapping(path = "/delete/{id}")
    public String deleteRun(@PathVariable("id") String runId) {
        return runService.deleteRun(Long.parseLong(runId));
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping(path = "/enroll/{id}")
    public String enrollRun(@PathVariable("id") String runId) {
        return runService.enrollRun(Long.parseLong(runId));
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping(path = "/unenroll/{id}")
    public String unenrollRun(@PathVariable("id") String runId) {
        return runService.unenrollRun(Long.parseLong(runId));
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping(path = "/watch/{id}")
    public String watchRun(@PathVariable("id") String runId) {
        return runService.watchRun(Long.parseLong(runId));
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping(path = "/unwatch/{id}")
    public String unwatchRun(@PathVariable("id") String runId) {
        return runService.unwatchRun(Long.parseLong(runId));
    }

}
