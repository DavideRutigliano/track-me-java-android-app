package com.github.ferrantemattarutigliano.software.server.controller;

import com.github.ferrantemattarutigliano.software.server.constant.Message;
import com.github.ferrantemattarutigliano.software.server.constant.Role;
import com.github.ferrantemattarutigliano.software.server.model.dto.PositionDTO;
import com.github.ferrantemattarutigliano.software.server.model.dto.RunDTO;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.Position;
import com.github.ferrantemattarutigliano.software.server.model.entity.Run;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.RunRepository;
import com.github.ferrantemattarutigliano.software.server.repository.UserRepository;
import com.github.ferrantemattarutigliano.software.server.service.RunService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;

public class RunControllerTest {

    @InjectMocks
    private RunController runController;
    @Mock
    private RunService mockRunService;
    @Mock
    private SecurityContext mockSecurityContext;
    @Mock
    private Authentication mockAuthentication;
    @Mock
    private Principal mockPrincipal;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private IndividualRepository mockIndividualRepository;
    @Mock
    private RunRepository mockRunRepository;

    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
    }

    private void mockIndividualAuthorized(User expectedUser, Individual expectedIndividual) {
        SecurityContextHolder.setContext(mockSecurityContext);

        Mockito.when(mockSecurityContext.getAuthentication())
                .thenReturn(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal())
                .thenReturn(mockPrincipal);
        Mockito.when(mockSecurityContext.getAuthentication().getPrincipal())
                .thenReturn(expectedUser);
        Mockito.when(mockUserRepository.existsByUsername(expectedUser.getUsername()))
                .thenReturn(true);
        //mock the existing individual associated with the user
        Mockito.when(mockIndividualRepository.existsByUser(expectedUser))
                .thenReturn(true);
        Mockito.when(mockIndividualRepository.findByUser(expectedUser))
                .thenReturn(expectedIndividual);
    }

    private Run createMockRun(Individual organizer, String latitude, String longitude) {
        Run run = new Run();
        Date date = new Date(1);
        Time time = new Time(1);
        run.setState("created");
        run.setTitle("marathon");
        run.setDate(date);
        run.setTime(time);
        run.setPath("[com.github.ferrantemattarutigliano.software.server.model.dto.PositionDTO@3eb631b8]");
        //  run.setPath(latitude + ":" + longitude + ";" );
        return run;

    }

    private RunDTO createMockRunDTO() {
        RunDTO run = new RunDTO();
        Date date = new Date(1);
        Time time = new Time(1);
        run.setState("created");
        run.setTitle("marathon");
        run.setDate(date);
        //create path collection
        run.setTime(time);
        PositionDTO path = new PositionDTO();
        path.setLatitude(10.0);
        path.setLongitude(50.0);
        Collection<PositionDTO> paths = new ArrayList<>();
        paths.add(path);
        run.setPath(paths);
        return run;

    }


    @Test
    public void createRunTest() {
        // RunDTO creation
        RunDTO firstRunDTO = createMockRunDTO();
        //create a mock user
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        //create mock Run
        Run firstRun = createMockRun(mockedIndividual, "10.0", "50.0");
        firstRun.setState("created");


        //TEST STARTS HERE

        Mockito.when(mockRunService.createRun(any(Run.class)))
                .thenReturn(Message.RUN_CREATED.toString());

        String result = runController.createRun(firstRunDTO);

        Assert.assertEquals(Message.RUN_CREATED.toString(), result);
    }
}
