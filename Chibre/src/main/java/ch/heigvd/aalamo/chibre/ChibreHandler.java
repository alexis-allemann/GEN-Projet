/* ---------------------------
Laboratoire : 04
Fichier :     BalloonHandler.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        26.03.2020 - 01.04.2020
But : Classe représentant le handler d'un ballon
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
     * Instanciation d'un handler de ballon
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
     * Méthode du thread qui écoute le réseau pour savoir si un ballon est reçu
     */
    @Override
    public void run() {
        BalloonJPanel balloonJPanel;
        try {
            while ((balloonJPanel = (BalloonJPanel) in.readObject()) != null) {
                chibreServer.send(balloonJPanel);
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
     * Envoi d'un ballon sur le réseau
     *
     * @param balloonJPanel le ballon
     * @throws IOException s'il y a une erreur de donnée
     */
    public void send(BalloonJPanel balloonJPanel) throws IOException {
        out.writeObject(balloonJPanel);
    }
}
