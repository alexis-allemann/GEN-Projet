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
import ch.heigvd.aalamo.chibre.network.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread {
    // Attributs
    private List<Game> games = new ArrayList<>();
    private List<Player> waitingPlayers = new ArrayList<>();

    /**
     * Instancitation du thread du serveur
     */
    @Override
    public void run(){
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
            Player newPlayer = new Player(newHandler);
            waitingPlayers.add(newPlayer);

            // S'il y a assez de joueurs en attente, on crée un partie
            if (waitingPlayers.size() == Game.NB_PLAYERS)
                createNewGame();
        }
    }

    /**
     * Création d'une partie avec les joueurs en attente
     */
    private void createNewGame() {
        // TODO : voir si créer thread pour éviter que un utilisateur se déconnecte pendant que on crée la partie et que du coup on ait un out_of_bound
        List<Player> players = new ArrayList<>(Game.NB_PLAYERS);
        for (int i = 0; i < Game.NB_PLAYERS; ++i) {
            players.add(waitingPlayers.get(0));
            waitingPlayers.remove(0);
        }
        Game game = new Game(players);
        game.startGame();
        games.add(game);
    }

    /**
     * Supprimer le handler d'un utilisateur si sa session à été fermée
     *
     * @param player le joueur
     */
    public void remove(Player player) {
        waitingPlayers.remove(player);
    }

    /**
     * @return la liste des parties en cours
     */
    public List<Game> getGames() {
        return games;
    }

    /**
     * @return la liste des joueurs en attente
     */
    public List<Player> getWaitingPlayers() {
        return waitingPlayers;
    }
}
