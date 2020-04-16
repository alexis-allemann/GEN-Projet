package ch.heigvd.aalamo.chibre.backend;

import ch.heigvd.aalamo.chibre.frontend.ChibreUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerTest {
    @BeforeEach
    public void instancierServeur(){
        Server server = new Server();
    }

    @Test
    public void waitingUserTest() {
        Server server = new Server();
        ChibreUser user1 = new ChibreUser();
        ChibreUser user2 = new ChibreUser();
        ChibreUser user3 = new ChibreUser();

        // Vérification que les joueurs sont en attente
        assertTrue(server.getWaitingPlayers().size() == 3);
        assertTrue(server.getWaitingPlayers().contains(user1));
        assertTrue(server.getWaitingPlayers().contains(user2));
        assertTrue(server.getWaitingPlayers().contains(user3));

        // Ajout d'un nouveau joueur => la partie démarre
        ChibreUser user4 = new ChibreUser();

        // Plus de joueur en attente
        assertTrue(server.getWaitingPlayers().size() == 0);
    }

    @Test
    public void gamesIdTest() {

    }
}
