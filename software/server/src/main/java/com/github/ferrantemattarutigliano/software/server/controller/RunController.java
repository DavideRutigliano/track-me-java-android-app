package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.dto.DTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.PositionDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.RunDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.Position;
import com.github.ferrantemattarutigliano.software.server.model.entity.Run;
import com.github.ferrantemattarutigliano.software.server.service.RunService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping(path = "/run")
public class RunController {

    @Autowired
    private RunService runService;

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping(path = "/create")
    public String createRun(@RequestBody RunDTO runDto) {
        ModelMapper modelMapper = new ModelMapper();
        Run run = modelMapper.map(runDto, Run.class);

        String pathString = "";
        Collection<PositionDTO> pathDto = runDto.getPath();

        for (PositionDTO positionDTO : pathDto) {
            Position position = modelMapper.map(positionDTO, Position.class);
            pathString += position.getLatitude() + ":" + position.getLongitude() + ";";
        }

        run.setPath(pathString);
        run.setState("created");

        return runService.createRun(run);
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @GetMapping(path = "/show")
    public Collection<RunDTO> showRuns() {
        return convertRuns(runService.showRuns());
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @GetMapping(path = "/show/new")
    public Collection<RunDTO> showNewRuns() {
        return convertRuns(runService.showNewRuns());
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @GetMapping(path = "/show/created")
    public Collection<RunDTO> showCreatedRuns() {
        return convertRuns(runService.showCreatedRuns());
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @GetMapping(path = "/show/enrolled")
    public Collection<RunDTO> showEnrolledRuns() {
        return convertRuns(runService.showEnrolledRuns());
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @GetMapping(path = "/show/watched")
    public Collection<RunDTO> showWatchedRuns() {
        return convertRuns(runService.showWatchedRuns());
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PutMapping(path = "/start/{id}")
    public String startRun(@PathVariable("id") String runId) {
        return runService.startRun(Long.parseLong(runId));
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PutMapping(path = "/edit/{id}")
    public String editRun(@RequestBody @DTO(RunDTO.class) Run run) {
        return runService.editRun(run);
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @DeleteMapping(path = "/delete/{id}")
    public String deleteRun(@PathVariable("id") String runId) {
        return runService.deleteRun(Long.parseLong(runId));
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PutMapping(path = "/enroll/{id}")
    public String enrollRun(@PathVariable("id") String runId) {
        return runService.enrollRun(Long.parseLong(runId));
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PutMapping(path = "/unenroll/{id}")
    public String unenrollRun(@PathVariable("id") String runId) {
        return runService.unenrollRun(Long.parseLong(runId));
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PutMapping(path = "/watch/{id}")
    public String watchRun(@PathVariable("id") String runId) {
        return runService.watchRun(Long.parseLong(runId));
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PutMapping(path = "/unwatch/{id}")
    public String unwatchRun(@PathVariable("id") String runId) {
        return runService.unwatchRun(Long.parseLong(runId));
    }

    private Collection<RunDTO> convertRuns(Collection<Run> runs) {
        Collection<RunDTO> runDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for (Run run : runs) {
            RunDTO runDto = modelMapper.map(run, RunDTO.class);
            String[] path = run.getPath().split(";");
            Collection<PositionDTO> positions = new ArrayList<>();
            for (String pos : path) {
                PositionDTO positionDto = new PositionDTO();
                positionDto.setLatitude(Double.parseDouble(StringUtils.substringBefore(pos, ":")));
                positionDto.setLongitude(Double.parseDouble(StringUtils.substringAfter(pos, ":")));
                positions.add(positionDto);
            }
            runDto.setPath(positions);
            runDTOS.add(runDto);
        }
        return runDTOS;
    }

}
