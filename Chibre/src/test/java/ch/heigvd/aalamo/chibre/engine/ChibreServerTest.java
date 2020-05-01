package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.ChibreController;
import ch.heigvd.aalamo.chibre.network.Server;
import ch.heigvd.aalamo.chibre.network.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChibreServerTest {
    @Test
    public void waitingUserTest() throws InterruptedException {
        Server server = new Server();
        server.start();
        ChibreController controller1 = new User();
        ChibreController controller2 = new User();
        ChibreController controller3 = new User();
        Thread.sleep(100);
        assertEquals(server.getWaitingPlayers().size(), 3);
    }

    @Test
    public void gamesIdTest() {
        assertTrue(true);
        // Tester ici si la génération des id en auto
    }

    //TODO
    // 1. Tester qu'un joueur entre dans la partie et attend qu'il y en ai 4
    // 2. Test que la partie se lance quand il y a 4joueurs
    // 3. Tester la distribution des cartes


}
