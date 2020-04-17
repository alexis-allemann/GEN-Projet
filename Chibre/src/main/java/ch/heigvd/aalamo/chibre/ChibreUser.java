/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreUser.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Programme de l'utilisateur
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

import ch.heigvd.aalamo.chibre.engine.User;
import ch.heigvd.aalamo.chibre.view.gui.GUIView;

public class ChibreUser {
    public static void main(String[] args) {
        try {
            // 1. Création du controlleur pour gérer le jeu du chibre
            ChibreController controller = new User();

            // 2. Création de la vue désirée
            ChibreView view = new GUIView(controller);

            // 3. Lancement du programme
            controller.start(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
