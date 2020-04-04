/* ---------------------------
Laboratoire : 04
Fichier :     BalloonUser.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        26.03.2020 - 01.04.2020
But : Classe représentant un utilisateur du ballon
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChibreUser {
    // Attributs
    private UserInterface userInterface;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Lancement d'une application pour un utilisateur
     * @param args arguments du programme (non requis)
     */
    public static void main(String[] args) {
        ChibreUser balloonUser = new ChibreUser();
        new ChibreJFrame(balloonUser);
        balloonUser.receive();
    }

    /**
     * Instancier un utilisateur de l'application
     */
    public ChibreUser() {
        try {
            socket = new Socket("localhost", 6666);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lecture du réseau afin de recevoir le ballon du serveur
     */
    public void receive() {
        BalloonJPanel balloonJPanel;
        try {
            while ((balloonJPanel = (BalloonJPanel) in.readObject()) != null) {
                // Affichage du ballon reçu
                userInterface.display(balloonJPanel);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Définition de l'interface utilisateur reliée à l'application
     * @param userInterface interface utilisateur
     */
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    /**
     * Envoi du ballon au serveur
     * @param balloonJPanel le ballon
     */
    public void send(BalloonJPanel balloonJPanel) {
        try {
            out.writeObject(balloonJPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
