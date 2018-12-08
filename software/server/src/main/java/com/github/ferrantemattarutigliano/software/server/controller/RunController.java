package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.service.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/Run")
public class RunController {

    @Autowired
    private RunService runService;

}
