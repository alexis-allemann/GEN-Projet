/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreController.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Interface du controller
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

public interface ChibreController {

    /**
     * Envoi de l'authentification au serveur
     *
     * @param username       nom de l'utilisateur
     * @param hashedPassword mot de passe haché
     */
    void sendAuthentication(String username, String hashedPassword);

    /**
     * Démarrer la GUI
     *
     * @param view la vue à démarrer
     */
    void start(ChibreView view);

    /**
     * Envoyer une carte au serveur
     */
    void sendCard(int index);

    /**
     * Envoyer la couleur atout choisie
     *
     * @param color couleur de l'atout
     */
    void chooseTrump(CardColor color);

    /**
     * Fermeture de la GUI
     */
    void quit();
}
