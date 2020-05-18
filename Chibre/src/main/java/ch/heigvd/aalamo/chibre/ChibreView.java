/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreView.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Interface de la vue
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

import ch.heigvd.aalamo.chibre.network.objects.DTOs.CardDTO;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.PlayerDTO;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.TeamDTO;
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
     * Action lors de l'erreur de création d'un utilisateur sur le serveur
     */
    void createUserFailed();

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
     * Définir le nom de l'utilisateur en cours
     *
     * @param userName nom de l'utilisateur
     */
    void setUserName(String userName);

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
     * Afficher la carte jouée par le joueur en cours
     *
     * @param card carte à afficher
     */
    void setBottomPlayerCard(CardDTO card);

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
     * Permet d'enlever l'affichage des cartes jouées
     */
    void resetPlayedCards();

    /**
     * Afficher les équipes et les joueurs de la partie
     */
    void displayTeams(TeamDTO team1, TeamDTO team2);

    /**
     * Afficher les joueurs autour du tapis de jeu
     *
     * @param players            les joueurs
     * @param currentPlayerIndex l'index du joueur qui joue sur la GUI
     */
    void displayPlayers(List<PlayerDTO> players, int currentPlayerIndex);

    /**
     * Afficher la carte jouée par un joueur
     *
     * @param card   la carte à afficher
     * @param player le joueur qui à jouer la carte
     */
    void displayCardPlayed(CardDTO card, PlayerDTO player);

    /**
     * Affichage des points des équipes
     *
     * @param pointsTeam1 points de l'équipe 1
     * @param pointsTeam2 points de l'équipe 2
     */
    void setPoints(int pointsTeam1, int pointsTeam2);

    /**
     * Définir le message d'information de l'état du jeu
     *
     * @param message à afficher
     */
    void setInfoMessage(String message);

    /**
     * Action à effectuer lorsque la fenêtre est fermée
     */
    void quit();
}
