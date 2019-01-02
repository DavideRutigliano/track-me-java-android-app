package com.github.ferrantemattarutigliano.software.server.service;

import com.github.ferrantemattarutigliano.software.server.constant.Role;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.Run;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import com.github.ferrantemattarutigliano.software.server.repository.IndividualRepository;
import com.github.ferrantemattarutigliano.software.server.repository.ThirdPartyRepository;
import com.github.ferrantemattarutigliano.software.server.repository.UserRepository;
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

import javax.print.attribute.standard.RequestingUserName;
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
    private UserRepository mockUserRepository;
    @Mock
    private IndividualRepository mockIndividualRepository;

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

/* //TODO if you need to mock third party. This isn't needed in this class.
    private void mockThirdPartyAuthorized(User expectedUser, ThirdParty expectedThirdParty) {
        SecurityContextHolder.setContext(mockSecurityContext);

        Mockito.when(mockSecurityContext.getAuthentication())
                .thenReturn(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal())
                .thenReturn(mockPrincipal);
        Mockito.when(mockSecurityContext.getAuthentication().getPrincipal())
                .thenReturn(expectedUser);
        Mockito.when(mockUserRepository.existsByUsername(expectedUser.getUsername()))
                .thenReturn(true);
        //mock the existing third party associated with the user
        Mockito.when(mockThirdPartyRepository.existsByUser(expectedUser))
                .thenReturn(true);
        Mockito.when(mockThirdPartyRepository.findByUser(expectedUser))
                .thenReturn(expectedThirdParty);
    }
*/

    private Run createMockRun(Individual organizer) {
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
        mockIndividualAuthorized(mockedUser, mockedIndividual);
        Mockito.when(mockIndividualRepository.findByUser(mockedUser))
                .thenReturn(mockedIndividual);

        Collection<Run> result = runService.showCreatedRuns();

        Assert.assertEquals(createdRuns, result);
    }

    @Test
    public void showNewRunsTest() {
        //create a mock user
        String role = Role.ROLE_INDIVIDUAL.toString();
        User mockedUser = new User("username", "password", "aa@aa.com", role);
        Individual mockedIndividual = new Individual();
        mockedIndividual.setUser(mockedUser);
        mockedIndividual.setFirstname("pippo");
        mockedIndividual.setLastname("pippetti");
        //create runs associated with the  user
        Run firstRun = createMockRun(mockedIndividual);
        Run secondRun = createMockRun(mockedIndividual);
        Run thirdRun = createMockRun(mockedIndividual);
        //create collections of runs
        Collection<Run> createdRuns = new ArrayList<>();
        createdRuns.add(firstRun);
        createdRuns.add(secondRun);
        createdRuns.add(thirdRun);
        Collection<Run> enrolledRuns = new ArrayList<>();
        enrolledRuns.add(firstRun);
        Collection<Run> watchedRuns = new ArrayList<>();
        watchedRuns.add(secondRun);
        //mock created, enrolled and watched runs in database
        mockedIndividual.setCreatedRuns(createdRuns);
        mockedIndividual.setEnrolledRuns(enrolledRuns);
        mockedIndividual.setWatchedRuns(watchedRuns);

        /* TEST STARTS HERE */
        mockIndividualAuthorized(mockedUser, mockedIndividual);
        Mockito.when(mockIndividualRepository.findByUser(mockedUser))
                .thenReturn(mockedIndividual);

        Collection<Run> result = runService.showNewRuns();
        //create collection with the expected result
        Collection<Run> expectedResult = new ArrayList<>();
        expectedResult.add(thirdRun);
        Assert.assertEquals(expectedResult, result);
    }


}