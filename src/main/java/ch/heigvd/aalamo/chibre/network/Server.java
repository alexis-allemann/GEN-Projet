/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreServer.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant le serveur du chibre
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.network;

import ch.heigvd.aalamo.chibre.engine.Game;
import ch.heigvd.aalamo.chibre.engine.Player;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.AuthenticationDTO;
import ch.heigvd.aalamo.chibre.network.objects.Request;
import ch.heigvd.aalamo.chibre.network.objects.ServerAction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread {
    // Attributs
    private String playersFileName;
    private List<Player> players = new ArrayList<>();
    private List<Game> games = new ArrayList<>();
    private List<Player> waitingPlayers = new ArrayList<>();
    private List<Handler> authenticatingHandlers = new ArrayList<>();

    /**
     * Instanciation du serveur
     */
    public Server(String playersFileName) {
        if (playersFileName.equals(""))
            this.playersFileName = "json/players.json";
        else
            this.playersFileName = playersFileName;

        // Chargement des joueurs depuis le fichier JSON
        loadPlayers();
    }

    // Getters

    /**
     * @return la liste des parties en cours
     */
    public List<Game> getGames() {
        return games;
    }

    /**
     * @return la liste des joueurs du serveur
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return la liste des joueurs en attente
     */
    public List<Player> getWaitingPlayers() {
        return waitingPlayers;
    }

    // Méthodes

    /**
     * Instancitation du thread du serveur
     */
    @Override
    public void run() {
        try {
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Action effectuée lorsque le serveur reçoit un nouveau handler sur le réseau
     *
     * @throws IOException s'il y a une erreur de donnée
     */
    private void receive() throws IOException {
        ServerSocket serverSocket = new ServerSocket(6666);
        while (true) {
            Socket socket = serverSocket.accept();
            Handler newHandler = new Handler(socket, this);
            authenticatingHandlers.add(newHandler);
        }
    }

    /**
     * Supprimer le handler d'un utilisateur si sa session à été fermée
     *
     * @param player le joueur
     */
    public void remove(Player player) {
        if (waitingPlayers.contains(player))
            waitingPlayers.remove(player);
    }

    /**
     * Récupérer un tableau de joueurs au format JSON
     *
     * @return le tableau des joueurs au format JSON
     */
    private JSONArray readPlayersFromJSON() {
        // JSON parser object pour lire le fichier des joueurs
        JSONParser jsonParser = new JSONParser();

        // Lecture du fichier
        try (FileReader reader = new FileReader(playersFileName)) {
            System.out.println("Lecture du fichier " + playersFileName);

            // Si le fichier n'est pas vide
            File file = new File(playersFileName);
            if (file.length() != 0) {
                // Lecture de l'objet JSON global
                Object obj = jsonParser.parse(reader);

                // Récupérer le tableau de joueurs
                return (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Chargement des joueurs du serveur depuis un fichier JSON
     */
    private void loadPlayers() {
        JSONArray players = readPlayersFromJSON();

        // Parcours du tableau de joueurs pour parsing
        players.forEach(country -> parsePlayerObject((JSONObject) country));
    }

    /**
     * Ajout d'un joueur dans le fichier JSON
     *
     * @param player joueur à ajouter
     */
    private void addPlayerToJSONFile(Player player) {
        JSONArray players = readPlayersFromJSON();

        // Création du nouvel objet avec le joueur à ajouter
        JSONObject newPlayer = new JSONObject();
        newPlayer.put("username", player.getUsername());
        newPlayer.put("password", player.getPassword());

        players.add(newPlayer);

        // Ecriture du fichier JSON
        try (FileWriter file = new FileWriter(playersFileName)) {
            System.out.println("Ecriture du fichier " + playersFileName);

            // Recupération de la liste des joueurs et écriture
            file.write(players.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parsing de l'objet JSON en joueur pour enregistrement sur le serveur
     *
     * @param player l'objet JSON représentant le joueur à parser
     */
    private void parsePlayerObject(JSONObject player) {
        String username = (String) player.get("username");
        String password = (String) player.get("password");
        players.add(new Player(username, password));
    }

    /**
     * Authentification d'une GUI à un joueur enregistré sur le serveur
     *
     * @param handler           reliée à la GUI qui essaie de s'authentifier
     * @param authenticationDTO objet de transfert de données pour l'authentification
     */
    public void authenticate(Handler handler, AuthenticationDTO authenticationDTO) {
        // Parcours de joueurs enregistrés
        for (Player player : players) {
            // Si on trouve une correspondance entre l'authentification et un joueur
            if (player.getUsername().equals(authenticationDTO.getUserName()) && player.getPassword().equals(authenticationDTO.getPassword())) {
                player.setHandler(handler);
                addPlayerToWaitingList(player);
                authenticatingHandlers.remove(handler);
                return;
            }
        }

        // Envoi de l'échec d'authentification à la GUI
        handler.sendRequest(new Request(ServerAction.AUTHENTICATION_FAILED));
    }

    /**
     * Ajout d'un joueur sur le serveur
     *
     * @param handler           reliée à la GUI qui essaie de s'authentifier
     * @param authenticationDTO objet de transfert de données pour l'authentification
     */
    public void createPlayer(Handler handler, AuthenticationDTO authenticationDTO) {
        Player newPlayer = new Player(authenticationDTO.getUserName(), authenticationDTO.getPassword());

        // On vérifie que aucun joueur n'a déjà le même nom d'utilisateur
        for (Player player : players) {
            if (player.getUsername().equals(authenticationDTO.getUserName())) {
                handler.sendRequest(new Request(ServerAction.CREATE_USER_FAILED));
                return;
            }
        }

        players.add(newPlayer);
        newPlayer.setHandler(handler);
        addPlayerToWaitingList(newPlayer);
        addPlayerToJSONFile(newPlayer);
    }

    /**
     * Ajout d'un joueur à la file d'attente
     *
     * @param player le joueur
     */
    private void addPlayerToWaitingList(Player player) {
        // Le joueur passe dans la liste d'attente des joueurs du serveur
        waitingPlayers.add(player);

        // On envoie à la GUI le joueur qui s'est connecté
        player.sendRequest(new Request(ServerAction.AUTHENTICATION_SUCCEED, player.serialize()));

        // S'il y a assez de joueurs en attente, on crée une partie
        if (waitingPlayers.size() == Game.NB_PLAYERS)
            createNewGame();
    }

    /**
     * Création d'une partie avec les joueurs en attente
     */
    private void createNewGame() {
        List<Player> players = new ArrayList<>(Game.NB_PLAYERS);
        for (int i = 0; i < Game.NB_PLAYERS; ++i)
            players.add(waitingPlayers.get(i));

        waitingPlayers.clear();
        Game game = new Game(players);
        games.add(game);
        game.run();
    }
}
