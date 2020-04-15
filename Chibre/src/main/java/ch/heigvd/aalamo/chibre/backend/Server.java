/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreServer.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant le serveur du chibre
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.backend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    // Attributs
    private List<Game> games = new ArrayList<>();
    private List<Player> waitingPlayers = new ArrayList<>();

    /**
     * Lancement d'une application pour un serveur
     *
     * @param args arguments du programme (non requis)
     */
    public static void main(String[] args) {
        try {
            new Server().receive();
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
            Player newPlayer = new Player(newHandler);
            newHandler.setPlayer(newPlayer);
            waitingPlayers.add(newPlayer);

            // S'il y a assez de joueurs en attente, on crée un partie
            if (waitingPlayers.size() == Game.NB_PLAYERS)
                createNewGame();
        }
    }

    private void createNewGame() {
        // TODO : voir si créer thread pour éviter que un utilisateur se déconnecte pendant que on crée la partie et que du coup on ait un out_of_bound
        List<Player> players = new ArrayList<>(Game.NB_PLAYERS);
        for (int i = 0; i < Game.NB_PLAYERS; ++i) {
            players.add(waitingPlayers.get(0));
            waitingPlayers.remove(0);
        }
        Game game = new Game(1, players); // TODO : générer IDs automatiquement
        game.startGame();
    }

    /**
     * Supprimer le handler d'un utilisateur si sa session à été fermée
     *
     * @param player le joueur
     */
    public void remove(Player player) {
        waitingPlayers.remove(player);
    }
}
