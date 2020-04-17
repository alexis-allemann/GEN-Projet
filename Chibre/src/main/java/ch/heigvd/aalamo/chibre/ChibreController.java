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
    // Ici on aura les actions envoyée de la GUI au controlleur

    /**
     * Démarrer la GUI
     *
     * @param view la vue à démarrer
     */
    void start(ChibreView view);

    /**
     * Envoyer une carte au serveur
     */
    void sendCard();
}