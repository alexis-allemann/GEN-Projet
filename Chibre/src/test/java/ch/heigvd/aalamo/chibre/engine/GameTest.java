package ch.heigvd.aalamo.chibre.engine;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private static Server server = new Server();

    @BeforeAll
    public static void setupTest() throws InterruptedException {
        server.start();
        for(int i = 0; i < 4; ++i)
            new User();
        Thread.sleep(100);
    }

    @AfterAll
    public static void finishTest(){
        server.stop();
    }

    @Test
    public void checkFourPlayersInGameTest(){
        assertEquals(server.getGames().get(0).getPlayers().size(),4);
    }

    @Test
    public void checkTwoPlayerInEachTeam(){
        boolean bothTeamHasTwoPlayers = true;
        for(Team team : server.getGames().get(0).getTeams())
            if (team.getPlayers().size() != 2) {
                bothTeamHasTwoPlayers = false;
                break;
            }

            assertTrue(bothTeamHasTwoPlayers);
    }

    @Test
    public void checkAutoIDTest() throws InterruptedException {
        for(int i = 0; i < 4; ++i)
            new User();
        Thread.sleep(100);
        boolean nextGameIsHigher = true;
        for(int i = 0; i < server.getGames().size() - 1; ++i)
            if (server.getGames().get(i).getId() >= server.getGames().get(i + 1).getId()) {
                nextGameIsHigher = false;
                break;
            }

        assertTrue(nextGameIsHigher);
    }

    @Test
    public void checkEachPlayerHasNineCards(){
        boolean everyBodyHasNineCards = true;
        for(Player player : server.getGames().get(0).getPlayers())
            if (player.getCards().size() != Game.NB_CARDS_PLAYER) {
                everyBodyHasNineCards = false;
                break;
            }
        assertTrue(everyBodyHasNineCards);
    }

}
