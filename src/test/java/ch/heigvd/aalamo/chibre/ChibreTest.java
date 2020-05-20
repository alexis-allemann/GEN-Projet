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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChibreTest {
    // Serveur pour les tests
    private static Server server = new Server("json/testplayers.json");

    @BeforeAll
    public static void resetJsonFile() {
        JSONArray players = new JSONArray();

        // Création du nouvel objet avec le joueur à ajouter
        for (int i = 1; i <= 4; ++i) {
            JSONObject newPlayer = new JSONObject();
            newPlayer.put("username", Integer.toString(i));
            newPlayer.put("password", User.toHexString(User.getSHA(Integer.toString(i))));
            players.add(newPlayer);
        }

        // Ecriture du fichier JSON
        try (FileWriter file = new FileWriter("json/testplayers.json", false)) {

            file.write(players.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    public static void setupTest() throws InterruptedException {
        server.start();
        for (int i = 1; i <= 4; ++i) {
            User user = new User();
            user.sendAuthentication(Integer.toString(i), Integer.toString(i));
        }
        Thread.sleep(1000);
    }

    @AfterAll
    public static void closeServer() {
        server.stop();
    }

    // Tests

    @Test
    public void waitingUserTest() throws InterruptedException {
        synchronized (server) {
            int nbWaitingPlayers = server.getWaitingPlayers().size();
            int nbPlayers = server.getPlayers().size();
            for (int i = 1; i <= 3; ++i) {
                User user = new User();
                user.sendCreateUser(Integer.toString(nbPlayers + i), Integer.toString(nbPlayers + i));
            }

            Thread.sleep(100);

            assertEquals(server.getWaitingPlayers().size(), (nbWaitingPlayers + 3) % 4);

            User user = new User();
            user.sendAuthentication(Integer.toString(nbPlayers + 4), Integer.toString(nbPlayers + 4));
            Thread.sleep(100);
            assertEquals(server.getWaitingPlayers().size(), nbWaitingPlayers);
        }
        notify();
    }

    @Test
    public void gameCreationTest() {
        assertFalse(server.getGames().isEmpty());
    }

    @Test
    public void checkFourPlayersInGameTest() {
        assertEquals(server.getGames().get(0).getPlayers().size(), 4);
    }

    @Test
    public void checkTwoPlayerInEachTeam() {
        boolean bothTeamHasTwoPlayers = true;
        for (Team team : server.getGames().get(0).getTeams())
            if (team.getPlayers().size() != 2) {
                bothTeamHasTwoPlayers = false;
                break;
            }

        assertTrue(bothTeamHasTwoPlayers);
    }

    @Test
    public void checkAutoIDTest() {
        boolean nextGameIsHigher = true;
        for (int i = 0; i < server.getGames().size() - 1; ++i)
            if (server.getGames().get(i).getId() >= server.getGames().get(i + 1).getId()) {
                nextGameIsHigher = false;
                break;
            }

        assertTrue(nextGameIsHigher);
    }

    @Test
    public void checkEachPlayerHasNineCards() {
        boolean everyBodyHasNineCards = true;
        for (Player player : server.getGames().get(0).getPlayers())
            if (player.getCards().size() != Game.NB_CARDS_PLAYER) {
                everyBodyHasNineCards = false;
                break;
            }

        assertTrue(everyBodyHasNineCards);
    }

    @Test
    public void checkRoundStart() {
        assertNotNull(server.getGames().get(0).getCurrentRound());
    }

    @Test
    public void checkTableCreation() {
        List<TablePosition> tablePositions = new ArrayList<>(TablePosition.values().length);
        tablePositions.addAll(Arrays.asList(TablePosition.values()));

        Table table = server.getGames().get(0).getTable();
        for (Player player : table.getPlayers()) {
            tablePositions.remove(table.getPositionByPlayer(player));
        }

        assertTrue(tablePositions.isEmpty());
    }

    @Test
    public void loadJSONFile() {
        List<Player> players = server.getPlayers();
        assertNotEquals(players, null);
        assertNotEquals(players.size(), 0);
    }

    @Test
    void createUser() throws InterruptedException {
        synchronized (server) {
            int nbUsers = server.getPlayers().size();
            User newUser = new User();
            newUser.sendCreateUser("99", "99");
            assertEquals(server.getPlayers().size(), nbUsers + 1);
        }
    }
}