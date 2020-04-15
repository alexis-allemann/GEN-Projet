/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreHandler.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un handler
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChibreHandler implements Runnable {
    // Attributs
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ChibreServer chibreServer;
    private Socket socket;

    /**
     * Instanciation d'un handler
     *
     * @param socket       interface réseau
     * @param chibreServer serveur
     * @throws IOException s'il y a une erreur de donnée
     */
    public ChibreHandler(Socket socket, ChibreServer chibreServer) throws IOException {
        this.chibreServer = chibreServer;
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
        CardJPanel cardJPanel;
        try {
            while ((cardJPanel = (CardJPanel) in.readObject()) != null) {
                chibreServer.send(cardJPanel);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
            chibreServer.remove(this);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Envoi d'une carte sur le réseau
     *
     * @param cardJPanel la carte
     * @throws IOException s'il y a une erreur de donnée
     */
    public void send(CardJPanel cardJPanel) throws IOException {
        out.writeObject(cardJPanel);
    }
}
