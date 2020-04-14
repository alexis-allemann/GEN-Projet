/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreServer.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant le serveur du chibre
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChibreServer {
    // Attributs
    private List<Game> games = new ArrayList<>();
    private List<ChibreHandler> waitingPlayers = new ArrayList<>();

    /**
     * Lancement d'une application pour un serveur
     *
     * @param args arguments du programme (non requis)
     */
    public static void main(String[] args) {
        try {
            new ChibreServer().receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Action effectuée lorsque le serveur recoit un nouveau handler sur le réseau
     *
     * @throws IOException s'il y a une erreur de donnée
     */
    private void receive() throws IOException {
        ServerSocket serverSocket = new ServerSocket(6666);
        while (true) {
            Socket socket = serverSocket.accept();
            ChibreHandler newHandler = new ChibreHandler(socket, this);
            waitingPlayers.add(newHandler);

            // S'il y a assez de joueurs en attente, on crée un partie
            if (waitingPlayers.size() == Game.NB_PLAYERS)
                createNewGame();
        }
    }

    private void createNewGame() {
        // TODO : voir si créer thread pour éviter que un utilisateur se déconnecte pendant que on crée la partie et que du coup on ait un out_of_bound
        Game game = new Game(1); // TODO : générer IDs automatiquement
        for (int i = 0; i < Game.NB_PLAYERS; ++i) {
            game.addPlayer(waitingPlayers.get(0));
            waitingPlayers.remove(0);
        }
        game.startGame();
    }

    /**
     * @param cardJPanel la carte
     * @throws IOException s'il y a une erreur de donnée
     */
    public void send(CardJPanel cardJPanel) throws IOException {

    }

    /**
     * Supprimer le handler d'un utilisateur si sa session à été fermée
     *
     * @param chibreHandler le handler
     */
    public void remove(ChibreHandler chibreHandler) {
        if (waitingPlayers.contains(chibreHandler))
            waitingPlayers.remove(chibreHandler);
        // Si le joueur est dans une partie, on le laisse jouer (le timer jouera un carte aléatoire pour lui)
    }
}
