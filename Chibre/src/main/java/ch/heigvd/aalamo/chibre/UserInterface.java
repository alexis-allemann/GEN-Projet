/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     UserInterface.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Interface représentant l'interface graphique de l'application
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

public interface UserInterface {
    /**
     * Affichage de la carte dans l'interface utilisateur
     * @param cardJPanel la carte à afficher
     */
    void display(CardJPanel cardJPanel);

    /**
     * Action effectuée lorsque la fenêtre est fermée
     */
    void quit();
}