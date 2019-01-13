package com.github.ferrantemattarutigliano.software.server;

import com.github.ferrantemattarutigliano.software.server.config.SecurityConfig;
import com.github.ferrantemattarutigliano.software.server.config.WebSocketConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class,
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(value = "classpath:application.properties")
@ContextConfiguration(classes = {SecurityConfig.class,
                                WebSocketConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
                        DirtiesContextTestExecutionListener.class,
                        TransactionalTestExecutionListener.class})
public class ServerApplicationTest {
    @Test
    public void contextLoads() {
        ServerApplication.main(new String[] {});
    }
}