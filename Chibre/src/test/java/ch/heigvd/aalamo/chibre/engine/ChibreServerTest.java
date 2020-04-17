package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.ChibreController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChibreServerTest {

    private static Server server = new Server();

    @BeforeAll
    public static void setupTest(){
        server.start();
    }

    @AfterAll
    public static void finishTest(){
        server.stop();
    }

    @Test
    public void waitingUserTest() throws InterruptedException {

        for(int i = 0; i < 3; ++i)
            new User();

        Thread.sleep(100);
        assertEquals(server.getWaitingPlayers().size(), 3);
    }

    @Test
    public void gameCreationTest() throws InterruptedException {

        for(int i = 0; i < 4; ++i)
            new User();

        Thread.sleep(100);
        assertFalse(server.getGames().isEmpty());
    }
}
