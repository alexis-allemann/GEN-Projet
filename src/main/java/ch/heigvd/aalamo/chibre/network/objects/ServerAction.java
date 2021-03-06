/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ServerAction.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Enumération des actions demandées par le serveur à la GUI
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.network.objects;

import java.io.Serializable;

public enum ServerAction implements Serializable {
    AUTHENTICATION_FAILED,
    AUTHENTICATION_SUCCEED,
    CREATE_USER_FAILED,
    SEND_TEAMS,
    SEND_CARDS,
    ASK_TRUMP,
    SEND_ANNOUCEMENTS,
    SEND_TRUMP_PLAYER,
    SEND_TRUMP_COLOR,
    SEND_CARD_PLAYED,
    SEND_CURRENT_PLAYER,
    SEND_POINTS,
    SEND_RESET_CARDS,
    SEND_WINNING_PLAYER,
    END_ROUND,
    SEND_CHIBRE,
    SEND_WINNER,
    DISPLAY_ANNOUNCEMENT,
    WINNING_ANNOUNCEMENT
}
