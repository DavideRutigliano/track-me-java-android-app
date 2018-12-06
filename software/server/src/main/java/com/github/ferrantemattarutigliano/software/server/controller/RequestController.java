package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.model.dto.CollectionDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.DTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    @GetMapping("/{username}/indiviudal")
    public @CollectionDTO(IndividualRequestDTO.class)
    Collection<IndividualRequest> showSentIndividualRequest(@PathVariable("username") String username,
                                                            Long id) {

        return requestService.showSentIndividualRequest(id);
    }

    @PreAuthorize("hasRole('THIRD_PARTY')")
    @GetMapping("/{username}/group")
    public @CollectionDTO(GroupRequestDTO.class)
    Collection<GroupRequest> showSentGroupRequest(@PathVariable("username") String username,
                                                  Long id) {

        return requestService.showSentGroupRequest(id);
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @GetMapping("/{username}/incoming")
    public @CollectionDTO(IndividualRequestDTO.class)
    Collection<IndividualRequest> showIncomingRequest(@PathVariable("username") String username,
                                                      String ssn) {

        return requestService.showIncomingRequest(ssn);
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PutMapping("/{username}/{id}")
    public String handleRequest(@PathVariable("username") String username,
                                @PathVariable("id") Long id,
                                Boolean accepted) {

        requestService.handleRequest(id, accepted);
        return "Success!";
    }
}
