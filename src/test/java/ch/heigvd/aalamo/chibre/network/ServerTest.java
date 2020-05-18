package ch.heigvd.aalamo.chibre.network;

import ch.heigvd.aalamo.chibre.engine.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ServerTest {
    Server server = new Server();

    @Test
    public void loadJSONFile() throws InterruptedException {
        List<Player> players = server.getPlayers();
        assertNotEquals(players, null);
        assertNotEquals(players.size(), 0);
    }
}
