/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreTest.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe permettant de faire les tests unitaires
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

import ch.heigvd.aalamo.chibre.engine.*;
import ch.heigvd.aalamo.chibre.network.Server;
import ch.heigvd.aalamo.chibre.network.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChibreTest {
    private static Server server = new Server();

    @BeforeAll
    public static void setupTest() throws InterruptedException {
        server.start();
    }

    @AfterAll
    public static void finishTest() {
        server.stop();
    }

    @Test
    public void waitingUserTest() throws InterruptedException {

        for (int i = 0; i < 3; ++i)
            new User();

        Thread.sleep(100);
        assertEquals(server.getWaitingPlayers().size(), 3);
    }

    @Test
    public void gameCreationTest() throws InterruptedException {

        for (int i = 0; i < 4; ++i)
            new User();

        Thread.sleep(100);
        assertFalse(server.getGames().isEmpty());
    }

    @Test
    public void checkFourPlayersInGameTest() throws InterruptedException {
        for (int i = 0; i < 4; ++i)
            new User();
        Thread.sleep(100);
        assertEquals(server.getGames().get(0).getPlayers().size(), 4);
    }

    @Test
    public void checkTwoPlayerInEachTeam() throws InterruptedException {
        for (int i = 0; i < 4; ++i)
            new User();
        Thread.sleep(100);
        boolean bothTeamHasTwoPlayers = true;
        for (Team team : server.getGames().get(0).getTeams())
            if (team.getPlayers().size() != 2) {
                bothTeamHasTwoPlayers = false;
                break;
            }

        assertTrue(bothTeamHasTwoPlayers);
    }

    @Test
    public void checkAutoIDTest() throws InterruptedException {
        for (int i = 0; i < 8; ++i)
            new User();
        Thread.sleep(100);
        boolean nextGameIsHigher = true;
        for (int i = 0; i < server.getGames().size() - 1; ++i)
            if (server.getGames().get(i).getId() >= server.getGames().get(i + 1).getId()) {
                nextGameIsHigher = false;
                break;
            }

        assertTrue(nextGameIsHigher);
    }

    @Test
    public void checkEachPlayerHasNineCards() throws InterruptedException {
        List<User> users = new ArrayList<>(4);
        for (int i = 0; i < 4; ++i) {
            User currentUser = new User();
            users.add(currentUser);
            Thread.sleep(100);
            //currentUser.sendPlayerName("toto" + i);
        }

        Thread.sleep(100);
        System.out.println("test");
        boolean everyBodyHasNineCards = true;
        // TODO : voir pourquoi plusieurs games
        for (Player player : server.getGames().get(1).getPlayers())
            if (player.getCards().size() != Game.NB_CARDS_PLAYER) {
                everyBodyHasNineCards = false;
                break;
            }

        assertTrue(everyBodyHasNineCards);
    }

    @Test
    public void checkRoundStart() throws InterruptedException {
        List<User> users = new ArrayList<>(4);
        for (int i = 0; i < 4; ++i) {
            User currentUser = new User();
            users.add(currentUser);
            Thread.sleep(100);
            //currentUser.sendPlayerName("toto" + i);
        }

        // TODO : voir pourquoi plusieurs games
        assertNotNull(server.getGames().get(1).getCurrentRound());
    }

    @Test
    public void checkTableCreation() throws InterruptedException {
        for (int i = 0; i < 4; ++i)
            new User();

        Thread.sleep(100);
        List<TablePosition> tablePositions = new ArrayList<>(TablePosition.values().length);
        tablePositions.addAll(Arrays.asList(TablePosition.values()));

        Table table = server.getGames().get(0).getTable();
        for (Player player : table.getPlayers()) {
            tablePositions.remove(table.getPositionByPlayer(player));
        }

        assertTrue(tablePositions.isEmpty());

    }
}