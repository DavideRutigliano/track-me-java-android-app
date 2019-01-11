
package com.github.ferrantemattarutigliano.software.server;


import com.github.ferrantemattarutigliano.software.server.config.SecurityConfig;
import com.github.ferrantemattarutigliano.software.server.config.WebSocketConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {SecurityConfig.class, WebSocketConfig.class})
public class ServerApplicationTest {
    @Test
    public void contextLoads() {
        ServerApplication.main(new String[] {});
    }
}
