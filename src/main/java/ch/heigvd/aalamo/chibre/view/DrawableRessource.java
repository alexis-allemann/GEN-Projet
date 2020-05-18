/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     DrawableRessource.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une partie de Chibre
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.view;

public interface DrawableRessource<Resource> {
    /**
     * Chargement des ressources du jeu
     *
     * @return les ressources
     */
    Resource getResource();
}
