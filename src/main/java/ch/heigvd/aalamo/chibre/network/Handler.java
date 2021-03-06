/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreHandler.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un handler
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.network;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.engine.Announcement;
import ch.heigvd.aalamo.chibre.engine.Card;
import ch.heigvd.aalamo.chibre.engine.Player;
import ch.heigvd.aalamo.chibre.network.objects.*;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.AnnouncementDTO;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.AuthenticationDTO;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.CardDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Handler implements Runnable {
    // Attributs
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Server server;
    private Player player;
    private final Socket socket;

    /**
     * Instanciation d'un handler
     *
     * @param socket interface réseau
     * @param server serveur
     * @throws IOException s'il y a une erreur de donnée
     */
    public Handler(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        new Thread(this).start();
    }

    // Setters

    /**
     * Définir le joueur relié au handler
     *
     * @param player le joueur
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    // Méthodes

    /**
     * Méthode du thread qui écoute le réseau et qui envoie les données au server
     */
    @Override
    public void run() {
        Response response;
        try {
            while ((response = (Response) in.readObject()) != null) {
                switch (response.getAction()) {
                    case AUTHENTICATION:
                        server.authenticate(this, (AuthenticationDTO) response.getObject());
                        break;
                    case CREATE_USER:
                        server.createPlayer(this, (AuthenticationDTO) response.getObject());
                        break;
                    case SEND_ANNOUCEMENT:
                        AnnouncementDTO announcementDTO = (AnnouncementDTO) response.getObject();
                        Announcement announcement = player.getGame().getCurrentRound().getAnnouncementByID(announcementDTO.getId());
                        player.getGame().getCurrentRound().addAnnouncement(announcement);
                        break;
                    case PLAY_CARD:
                        CardDTO card = (CardDTO) response.getObject();
                        Card cardPlayed = player.getCardWithId(card.getId());
                        player.getGame().getCurrentRound().getCurrentTurn().playCard(cardPlayed);
                        player.getGame().getCurrentRound().getCurrentTurn().pursueTurn();
                        break;
                    case SEND_TRUMP:
                        CardColor color = (CardColor) response.getObject();
                        if (color == null) {
                            player.getGame().sendToAllPlayers(new Request(ServerAction.SEND_CHIBRE, player.getTeam().getOtherPlayer(player).serialize()));
                            player.getTeam().getOtherPlayer(player).sendRequest(new Request(ServerAction.ASK_TRUMP));
                        } else {
                            player.getGame().getCurrentRound().setTrumpColor(color);
                            player.getGame().getCurrentRound().initAnnoucement();
                            player.getGame().getCurrentRound().initTurn();
                        }
                        break;
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        // Lorsque le joueur se déconnecte
        try {
            socket.close();
            // Notifier le serveur que le joueur n'est plus disponible
            if (player != null)
                server.remove(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envoi d'une requête à la GUI
     *
     * @param request requête à envoyer
     */
    public void sendRequest(Request request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
