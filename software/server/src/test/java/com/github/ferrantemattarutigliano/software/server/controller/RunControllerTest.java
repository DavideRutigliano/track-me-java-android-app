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
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.internal.progress.SequenceNumber.next;

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
        run.setPath("10.0:50.0;");
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

    @Test
    public void showRunsTest() {
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
        Run secondRun = createMockRun(mockedIndividual, "20.0", "20.0");
        firstRun.setState("created");
        secondRun.setState("created");
        //add to a collection
        Collection<Run> orgRuns = new ArrayList<>();
        orgRuns.add(firstRun);
        orgRuns.add(secondRun);
        //add to organizer
        mockedIndividual.setCreatedRuns(orgRuns);


        //TEST STARTS HERE

        Mockito.when(mockRunService.showRuns())
                .thenReturn(orgRuns);

        Collection<RunDTO> result = runController.showRuns();

        Collection<RunDTO> expRuns = convertRuns(orgRuns);
        Iterator<RunDTO> RDE = expRuns.iterator();
        for (Iterator<RunDTO> i = result.iterator(); i.hasNext(); ) {
            RunDTO RD = i.next();
            RunDTO h = RDE.next();
            h.setPath(RD.getPath());
            Assert.assertEquals(RD.getId(), h.getId());
            Assert.assertEquals(RD.getPath(), h.getPath());
            Assert.assertEquals(RD.getDate(), h.getDate());
            Assert.assertEquals(RD.getTime(), h.getTime());
            Assert.assertEquals(RD.getState(), h.getState());
            Assert.assertEquals(RD.getTitle(), h.getTitle());
        }

    }

    @Test
    public void showNewRunsTest() {
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
        Run secondRun = createMockRun(mockedIndividual, "20.0", "20.0");
        firstRun.setState("created");
        secondRun.setState("created");
        //add to a collection
        Collection<Run> orgRuns = new ArrayList<>();
        orgRuns.add(firstRun);
        orgRuns.add(secondRun);
        //add to organizer
        mockedIndividual.setCreatedRuns(orgRuns);


        //TEST STARTS HERE

        Mockito.when(mockRunService.showNewRuns())
                .thenReturn(orgRuns);

        Collection<RunDTO> result = runController.showNewRuns();

        Collection<RunDTO> expRuns = convertRuns(orgRuns);
        Iterator<RunDTO> RDE = expRuns.iterator();
        for (Iterator<RunDTO> i = result.iterator(); i.hasNext(); ) {
            RunDTO RD = i.next();
            RunDTO h = RDE.next();
            h.setPath(RD.getPath());
            Assert.assertEquals(RD.getId(), h.getId());
            Assert.assertEquals(RD.getPath(), h.getPath());
            Assert.assertEquals(RD.getDate(), h.getDate());
            Assert.assertEquals(RD.getTime(), h.getTime());
            Assert.assertEquals(RD.getState(), h.getState());
            Assert.assertEquals(RD.getTitle(), h.getTitle());
        }

    }

    @Test
    public void showCreatedRunsTest() {
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
        Run secondRun = createMockRun(mockedIndividual, "20.0", "20.0");
        firstRun.setState("created");
        secondRun.setState("created");
        //add to a collection
        Collection<Run> orgRuns = new ArrayList<>();
        orgRuns.add(firstRun);
        orgRuns.add(secondRun);
        //add to organizer
        mockedIndividual.setCreatedRuns(orgRuns);


        //TEST STARTS HERE

        Mockito.when(mockRunService.showCreatedRuns())
                .thenReturn(orgRuns);

        Collection<RunDTO> result = runController.showCreatedRuns();

        Collection<RunDTO> expRuns = convertRuns(orgRuns);
        Iterator<RunDTO> RDE = expRuns.iterator();
        for (Iterator<RunDTO> i = result.iterator(); i.hasNext(); ) {
            RunDTO RD = i.next();
            RunDTO h = RDE.next();
            h.setPath(RD.getPath());
            Assert.assertEquals(RD.getId(), h.getId());
            Assert.assertEquals(RD.getPath(), h.getPath());
            Assert.assertEquals(RD.getDate(), h.getDate());
            Assert.assertEquals(RD.getTime(), h.getTime());
            Assert.assertEquals(RD.getState(), h.getState());
            Assert.assertEquals(RD.getTitle(), h.getTitle());
        }

    }

    @Test
    public void showEnrolledRunsTest() {
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
        Run secondRun = createMockRun(mockedIndividual, "20.0", "20.0");
        firstRun.setState("created");
        secondRun.setState("created");
        //add to a collection
        Collection<Run> orgRuns = new ArrayList<>();
        orgRuns.add(firstRun);
        orgRuns.add(secondRun);
        //add to organizer and run enrolled
        mockedIndividual.setCreatedRuns(orgRuns);
        mockedIndividual.setEnrolledRuns(orgRuns);


        //TEST STARTS HERE

        Mockito.when(mockRunService.showEnrolledRuns())
                .thenReturn(orgRuns);

        Collection<RunDTO> result = runController.showEnrolledRuns();

        Collection<RunDTO> expRuns = convertRuns(orgRuns);
        Iterator<RunDTO> RDE = expRuns.iterator();
        for (Iterator<RunDTO> i = result.iterator(); i.hasNext(); ) {
            RunDTO RD = i.next();
            RunDTO h = RDE.next();
            h.setPath(RD.getPath());
            Assert.assertEquals(RD.getId(), h.getId());
            Assert.assertEquals(RD.getPath(), h.getPath());
            Assert.assertEquals(RD.getDate(), h.getDate());
            Assert.assertEquals(RD.getTime(), h.getTime());
            Assert.assertEquals(RD.getState(), h.getState());
            Assert.assertEquals(RD.getTitle(), h.getTitle());
        }

    }

    @Test
    public void showWatchedRunsTest() {
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
        Run secondRun = createMockRun(mockedIndividual, "20.0", "20.0");
        firstRun.setState("created");
        secondRun.setState("created");
        //add to a collection
        Collection<Run> orgRuns = new ArrayList<>();
        orgRuns.add(firstRun);
        orgRuns.add(secondRun);
        //add to organizer and run enrolled
        mockedIndividual.setCreatedRuns(orgRuns);
        mockedIndividual.setWatchedRuns(orgRuns);


        //TEST STARTS HERE

        Mockito.when(mockRunService.showWatchedRuns())
                .thenReturn(orgRuns);

        Collection<RunDTO> result = runController.showWatchedRuns();

        Collection<RunDTO> expRuns = convertRuns(orgRuns);
        Iterator<RunDTO> RDE = expRuns.iterator();
        for (Iterator<RunDTO> i = result.iterator(); i.hasNext(); ) {
            RunDTO RD = i.next();
            RunDTO h = RDE.next();
            h.setPath(RD.getPath());
            Assert.assertEquals(RD.getId(), h.getId());
            Assert.assertEquals(RD.getPath(), h.getPath());
            Assert.assertEquals(RD.getDate(), h.getDate());
            Assert.assertEquals(RD.getTime(), h.getTime());
            Assert.assertEquals(RD.getState(), h.getState());
            Assert.assertEquals(RD.getTitle(), h.getTitle());
        }

    }

    @Test
    public void startRunTest() {
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
        Run secondRun = createMockRun(mockedIndividual, "20.0", "20.0");
        firstRun.setState("created");
        firstRun.setId(0L);
        secondRun.setId(1L);
        secondRun.setState("created");
        //add to a collection
        Collection<Run> orgRuns = new ArrayList<>();
        orgRuns.add(firstRun);
        orgRuns.add(secondRun);
        //add to organizer and run enrolled
        mockedIndividual.setCreatedRuns(orgRuns);
        mockedIndividual.setWatchedRuns(orgRuns);


        //TEST STARTS HERE

        Mockito.when(mockRunService.startRun(0L))
                .thenReturn(Message.RUN_STARTED.toString());

        String result = runController.startRun("0");


        Assert.assertEquals(Message.RUN_STARTED.toString(), result);


    }


}
