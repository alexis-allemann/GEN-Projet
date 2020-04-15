/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreHandler.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un handler
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Handler implements Runnable {
    // Attributs
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Server server;
    private Player player;
    private Socket socket;

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

    /**
     * Méthode du thread qui écoute le réseau pour savoir si une carte est reçue
     */
    @Override
    public void run() {
        Card card;
        try {
            while ((card = (Card) in.readObject()) != null) {
                // game.send(cardJPanel);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
            // Notifier le serveur que le joueur n'est plus disponible
            if (player != null)
                server.remove(player);
            // TODO : voir si on notifie le player que le handler n'est plus disponible ou si on met un timer (par exemple)
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Définir le joueur relié au handler
     *
     * @param player le joueur
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Envoi d'une carte sur le réseau
     *
     * @param card la carte
     * @throws IOException s'il y a une erreur de donnée
     */
    public void sendCard(Card card) throws IOException {
        out.writeObject(card);
    }
}
