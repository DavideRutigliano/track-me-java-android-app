package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.dto.CollectionDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.DTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.HealthData;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path="/request")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @PostMapping("/individual")
    public String individualRequest(@RequestBody
                                    @DTO(IndividualRequestDTO.class)
                                            IndividualRequest individualRequest) {

        return requestService.individualRequest(individualRequest);
    }

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @PostMapping("/group")
    public String groupRequest(@RequestBody
                               @DTO(GroupRequestDTO.class)
                                       GroupRequest groupRequest) {

        return requestService.groupRequest(groupRequest);
    }

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @GetMapping("/{username}/individual")
    public @CollectionDTO(IndividualRequestDTO.class)
    Collection<IndividualRequest> showSentIndividualRequest(@PathVariable("username") String username) {

        return requestService.showSentIndividualRequest();
    }

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @GetMapping("/{username}/group")
    public @CollectionDTO(GroupRequestDTO.class)
    Collection<GroupRequest> showSentGroupRequest(@PathVariable("username") String username) {

        return requestService.showSentGroupRequest();
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @GetMapping("/{username}/received")
    public @CollectionDTO(IndividualRequestDTO.class)
    Collection<IndividualRequest> showIncomingRequest(@PathVariable("username") String username) {

        return requestService.showIncomingRequest();
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PutMapping("/{username}/incoming/{id}")
    public String handleRequest(@PathVariable("username") String username,
                                @PathVariable("id") Long id,
                                @RequestBody Boolean accepted) {

        return requestService.handleRequest(id, accepted);
    }

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @GetMapping(path = "/individual/{id}/data")
    public Collection<HealthData> showIndividualData(@PathVariable("id") String id,
                                                     @RequestBody @DTO(IndividualRequestDTO.class) IndividualRequest individualRequest) {
        return requestService.showIndividualData(individualRequest);
    }

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @GetMapping(path = "/group/{id}/data")
    public Collection<HealthData> showGroupData(@PathVariable("id") String id,
                                                @RequestBody @DTO(GroupRequestDTO.class) GroupRequest groupRequest) {
        return requestService.showGroupData(groupRequest);
    }
}
