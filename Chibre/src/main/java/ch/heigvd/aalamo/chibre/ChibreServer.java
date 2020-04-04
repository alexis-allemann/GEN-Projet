/* ---------------------------
Laboratoire : 04
Fichier :     BalloonServer.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        26.03.2020 - 01.04.2020
But : Classe représentant le serveur de gestion du ballon
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChibreServer {
    // Attributs
    private List<ChibreHandler> handlers = new ArrayList<>();
    private ChibreHandler handlerWithBalloon;
    private Random random = new Random();
    private BalloonJPanel balloon = new BalloonJPanel();

    /**
     * Lancement d'une application pour un serveur
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
     * Action effectuée lorsque le serveur recoit un ballon sur le réseau
     * @throws IOException s'il y a une erreur de donnée
     */
    private void receive() throws IOException {
        ServerSocket serverSocket = new ServerSocket(6666);
        while (true) {
            Socket socket = serverSocket.accept();
            ChibreHandler newHandler = new ChibreHandler(socket, this);
            handlers.add(newHandler);
            if (handlers.size() == 1) {
                newHandler.send(balloon);
                handlerWithBalloon = newHandler;
            }
        }
    }

    /**
     * Envoi du ballon à un utilisateur (pas celui qui vient d'envoyer le ballon sauf s'il est seul)
     * @param balloonJPanel le ballon
     * @throws IOException s'il y a une erreur de donnée
     */
    public void send(BalloonJPanel balloonJPanel) throws IOException {
        if (handlers.size() != 1) {
            ChibreHandler newHandler = getRandomHandler();
            while (handlerWithBalloon == newHandler)
                newHandler = getRandomHandler();
            handlerWithBalloon = newHandler;
        }
        handlerWithBalloon.send(balloonJPanel);
        balloon = balloonJPanel;
    }

    /**
     * Supprimer le handler d'un utilisateur si sa session à été fermée
     * @param balloonHandler le handler
     */
    public void remove(ChibreHandler balloonHandler) {
        handlers.remove(balloonHandler);
    }

    /**
     * Choix aléatorie d'un handler à qui envoyer le ballon
     * @return le handler tiré aléatoirement
     */
    private ChibreHandler getRandomHandler() {
        return handlers.get(random.nextInt(handlers.size()));
    }
}
