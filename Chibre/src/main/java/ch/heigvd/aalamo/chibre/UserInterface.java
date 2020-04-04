/* ---------------------------
Laboratoire : 04
Fichier :     UserInterface.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        26.03.2020 - 01.04.2020
But : Interface représentant un interface utilisateur de l'application
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

public interface UserInterface {
    /**
     * Affichage du ballon dans l'interface utilisateur
     * @param balloonJPanel le ballon à afficher
     */
    void display(BalloonJPanel balloonJPanel);

    /**
     * Action effectuée lorsque la fenêtre est fermée
     */
    void quit();
}