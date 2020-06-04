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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ChibreTest {
    // Serveur pour les tests
    private static Server server;

    /**
     * Initialisation du serveur et des joueurs avant les tests unitaires
     *
     * @throws InterruptedException si il y a une interruption lors de l'attente du login d'un joueur
     */
    @BeforeAll
    public static void initialiseTests() throws InterruptedException {
        // Remise à zéro du fichier JSON
        resetJSONFile();

        // Démarrer serveur
        server = new Server("json/testplayers.json");
        server.start();

        // Authentification de 4 utilisateurs
        for (int i = 1; i <= 4; ++i) {
            User user = new User();
            user.sendAuthentication(Integer.toString(i), Integer.toString(i));

            // Attente que le serveur reçoive l'authentification par le réseau pour éviter des problèmes de concurence
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    /**
     * Remettre à zéro l'état du fichier JSON de tests avant les tests unitaires
     */
    private static void resetJSONFile() {
        JSONArray players = new JSONArray();

        // Création du nouvel objet avec le joueur à ajouter (8 joueurs utilisés dans les tests)
        for (int i = 1; i <= 8; ++i) {
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

    // Tests

    /**
     * Utilisateurs en attente et création d'une partie
     *
     * @throws InterruptedException lors de la pause du thread si celui-ci est interrompu
     */
    @Test
    public void waitingUserTest() throws InterruptedException {
        int nbWaitingPlayers = server.getWaitingPlayers().size();

        // Authentification de 3 nouveaux joueurs
        for (int i = 5; i < 8; ++i) {
            User user = new User();
            user.sendAuthentication(Integer.toString(i), Integer.toString(i));
        }
        Thread.sleep(100);

        assertEquals(server.getWaitingPlayers().size(), (nbWaitingPlayers + 3) % 4);

        // Authentification d'un 4ème joueur
        User user = new User();
        user.sendAuthentication(Integer.toString(8), Integer.toString(8));

        Thread.sleep(100);

        assertEquals(server.getWaitingPlayers().size(), nbWaitingPlayers);
    }

    /**
     * Création d'une partie
     */
    @Test
    public void gameCreationTest() {
        assertFalse(server.getGames().isEmpty());
    }

    /**
     * 4 joueurs dans une partie
     */
    @Test
    public void checkFourPlayersInGameTest() {
        assertEquals(server.getGames().get(0).getPlayers().size(), 4);
    }

    /**
     * Deux joueurs exactement par équipes
     */
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

    /**
     * Identifiants automatiques sur le serveur
     */
    @Test
    public void checkAutoIDTest() {
        boolean nextGameIsHigher = true;
        for (int i = 0; i < server.getGames().size() - 1; ++i)
            if (server.getGames().get(i).getGameId() >= server.getGames().get(i + 1).getGameId()) {
                nextGameIsHigher = false;
                break;
            }

        assertTrue(nextGameIsHigher);
    }

    /**
     * Distribution des cartes équitable
     */
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

    /**
     * Démarrage d'un tour
     */
    @Test
    public void checkRoundStart() {
        assertNotNull(server.getGames().get(0).getCurrentRound());
    }

    /**
     * Création de la table d'une partie
     */
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

    /**
     * Lecture du fichier JSON contenant les joueurs
     */
    @Test
    public void loadJSONFile() {
        List<Player> players = server.getPlayers();
        assertNotEquals(players, null);
        assertNotEquals(players.size(), 0);
    }

    /**
     * Création d'un utilisateur
     *
     * @throws InterruptedException lors de la pause du thread si celui-ci est interrompu
     */
    @Test
    void createUser() throws InterruptedException {
        int nbUsers = server.getPlayers().size();
        User newUser = new User();
        newUser.sendCreateUser("99", "99");
        Thread.sleep(100);
        assertEquals(server.getPlayers().size(), nbUsers + 1);
    }
}