package com.github.ferrantemattarutigliano.software.server.controller;


import com.github.ferrantemattarutigliano.software.server.service.RequestService;
import com.github.ferrantemattarutigliano.software.server.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class IndividualDataController {

    @Autowired
    private RequestService requestor;

    @Autowired
    private SubscriptionService subscriptor;


}
