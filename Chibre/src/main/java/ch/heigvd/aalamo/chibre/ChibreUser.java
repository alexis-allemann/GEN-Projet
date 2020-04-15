/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreUser.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un utilisateur du chibre
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
     * Lecture du réseau afin de recevoir une carte
     */
    public void receive() {
        CardJPanel cardJPanel;
        try {
            while ((cardJPanel = (CardJPanel) in.readObject()) != null) {
                // Affichage de la carte reçue
                userInterface.display(cardJPanel);
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
     * Envoi d'une carte au serveur
     * @param cardJPanel la carte
     */
    public void send(CardJPanel cardJPanel) {
        try {
            out.writeObject(cardJPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
