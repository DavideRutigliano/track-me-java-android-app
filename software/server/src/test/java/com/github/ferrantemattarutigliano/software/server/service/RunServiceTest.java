package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.constant.Role;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.Run;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.RunRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

public class RunServiceTest {
    @InjectMocks
    private RunService runService;
    @Mock
    private SecurityContext mockSecurityContext;
    @Mock
    private Authentication mockAuthentication;
    @Mock
    private Principal mockPrincipal;
    @Mock
    private IndividualRepository mockIndividualRepository;

    @Before
    public void initTest(){
        MockitoAnnotations.initMocks(this);
    }

    private void mockSecurity(User expectedUser){
        SecurityContextHolder.setContext(mockSecurityContext);

        Mockito.when(mockSecurityContext.getAuthentication())
                .thenReturn(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal())
                .thenReturn(mockPrincipal);
        Mockito.when(mockSecurityContext.getAuthentication().getPrincipal())
                .thenReturn(expectedUser);
    }

    private Run createMockRun(Individual organizer){
        Run run = new Run();
        Date date = new Date(1);
        Time time = new Time(1);
        run.setState("created");
        run.setTitle("marathon");
        run.setDate(date);
        run.setTime(time);
        run.setOrganizer(organizer);
        return run;
    }

    @Test
    public void showCreatedRunsTest() {
        //create a mock user
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        //create runs with the associated user
        Run firstRun = createMockRun(mockedIndividual);
        Run secondRun = createMockRun(mockedIndividual);
        Run thirdRun = createMockRun(mockedIndividual);
        //pack them in a collection
        Collection<Run> createdRuns = new ArrayList<>();
        createdRuns.add(firstRun);
        createdRuns.add(secondRun);
        createdRuns.add(thirdRun);
        //mock created runs in database
        mockedIndividual.setCreatedRuns(createdRuns);

        /* TEST STARTS HERE */
        mockSecurity(mockedUser);
        Mockito.when(mockIndividualRepository.findByUser(mockedUser))
                .thenReturn(mockedIndividual);

        Collection<Run> result = runService.showCreatedRuns();

        Assert.assertEquals(createdRuns, result);
    }
}