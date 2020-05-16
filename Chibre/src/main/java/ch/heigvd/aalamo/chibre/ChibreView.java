/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreView.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Interface de la vue
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

import ch.heigvd.aalamo.chibre.engine.Card;
import ch.heigvd.aalamo.chibre.view.gui.UserChoice;

import java.util.List;

public interface ChibreView {

    /**
     * Afficher la fenêtre d'authentification
     */
    void displayAuthentication();

    /**
     * Afficher le message d'erreur d'authentification
     */
    void authenticationFailed();

    /**
     * Action lorsque l'authentification est correcte
     */
    void authenticationSucceed();

    /**
     * Action à effectuer lorsque la fenêtre est fermée
     */
    void quit();

    /**
     * Afficher une carte sur la vue
     *
     * @param type     type de la carte (as, roi, dame, valet, dix, neuf, huit, sept, six)
     * @param color    couleur de la carte (carreau, coeur, pique, trèfle)
     * @param position la posistion dans le deck de l'utilisateur (9 cartes => 0:8)
     */
    void addCard(CardType type, CardColor color, int position);

    /**
     * Poser une question à l'utilisateur dans une fenêtre
     *
     * @param title         titre de la fenêtre
     * @param question      la question
     * @param possibilities les options (liste déroulante)
     * @return l'option choisie au type défini
     */
    UserChoice askUser(String title, String question, List<UserChoice> possibilities);

    /**
     * Afficher un message à l'utilisateur
     *
     * @param title    titre du message
     * @param question question à poser
     * @return la réponse saisie
     */
    String askUser(String title, String question);

    /**
     * Définir le nom de l'utilisateur en cours
     *
     * @param userName nom de l'utilisateur
     */
    void setUserName(String userName);

    /**
     * Définir le nom de l'utilisateur à droite
     *
     * @param userName nom de l'utilisateur
     */
    void setRightPlayerName(String userName);

    /**
     * Définir le nom de l'utilisateur en haut
     *
     * @param userName nom de l'utilisateur
     */
    void setTopPlayerName(String userName);

    /**
     * Définir le nom de l'utilisateur à gauche
     *
     * @param userName nom de l'utilisateur
     */
    void setLeftPlayerName(String userName);

    /**
     * Affichage du joueur 1 de l'équipe 1
     *
     * @param userName nom de l'utilisateur
     */
    void setTeam1Player1(String userName);

    /**
     * Affichage du joueur 2 de l'équipe 1
     *
     * @param userName nom de l'utilisateur
     */
    void setTeam1Player2(String userName);

    /**
     * Affichage du joueur 1 de l'équipe 2
     *
     * @param userName nom de l'utilisateur
     */
    void setTeam2Player1(String userName);

    /**
     * Affichage du joueur 2 de l'équipe 2
     *
     * @param userName nom de l'utilisateur
     */
    void setTeam2Player2(String userName);

    /**
     * Affichage du joueur qui fait atout
     *
     * @param userName nom de l'utilisateur
     */
    void setTrumpPlayer(String userName);

    /**
     * Affichage de la couleur atout
     *
     * @param trumpColor couleur atout
     */
    void setTrumpColor(CardColor trumpColor);

    /**
     * Affichage du joueur qui a le tour
     *
     * @param userName joueur qui a le tour
     */
    void setCurrentPlayer(String userName);

    /**
     * Afficher la carte jouée par le joueur en haut
     *
     * @param card carte à afficher
     */
    void setTopPlayerCard(Card card);

    /**
     * Afficher la carte jouée par le joueur à gauche
     *
     * @param card carte à afficher
     */
    void setLeftPlayerCard(Card card);

    /**
     * Afficher la carte jouée par le joueur à droite
     *
     * @param card carte à afficher
     */
    void setRightPlayerCard(Card card);

    /**
     * Afficher la carte jouée par le joueur en cours
     *
     * @param card carte à afficher
     */
    void setBottomPlayerCard(Card card);

    /**
     * Redéfinir la carte jouée à vide
     */
    void resetBottomPlayerCard();

    /**
     * Afficher un message à l'utilisateur
     *
     * @param title   titre de la popup
     * @param message message à afficher
     */
    void showMessage(String title, String message);

    /**
     * Afficher les points de l'équipe 1 à l'utilisateur
     *
     * @param points points à afficher
     */
    void setPointsTeam1(int points);

    /**
     * Afficher les points de l'équipe 2 à l'utilisateur
     *
     * @param points points à afficher
     */
    void setPointsTeam2(int points);

    /**
     * Afficher l'équipe gagnante provisoire
     *
     * @param team team à afficher
     */
    void setWinningTeam(String team);

    /**
     * Permet d'enlever l'affichage des cartes jouées
     */
    void resetPlayedCards();
}
