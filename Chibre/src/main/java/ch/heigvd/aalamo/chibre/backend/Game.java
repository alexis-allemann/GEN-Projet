/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Game.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une partie de Chibre
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class Game {
    // Constantes globles du jeu
    public static final int NB_TEAMS = 2;
    public static final int NB_PLAYERS_TEAMS = 2;
    public static final int NB_CARDS = 36;
    public static final int NB_CARDS_PLAYER = 9;
    public static final int NB_PLAYERS = NB_TEAMS * NB_PLAYERS_TEAMS;

    // Attributs
    private int id;
    private List<Player> players;
    private List<Team> teams = new ArrayList<>(NB_TEAMS);
    private List<Round> rounds = new ArrayList<>();
    private List<Card> cards = new ArrayList<>(NB_CARDS);

    /**
     * Instancier une parte
     *
     * @param id      identifiant unique de la partie
     * @param players liste des joueurs
     */
    public Game(int id, List<Player> players) {
        if (players == null || players.contains(null))
            throw new IllegalArgumentException("Liste de joueurs illégale (nulle ou joueur nul)");

        this.id = id;
        this.players = players;
        setTeams(players);
    }

    /**
     * Définition des équipes à partir d'une liste de joueurs
     *
     * @param players les joueurs
     */
    private void setTeams(List<Player> players) {
        if (players.size() != NB_PLAYERS)
            throw new RuntimeException("Une partie ne peut pas être jouée à moins de " + NB_PLAYERS + " joueurs");

        // Mélange des joueurs pour choix aléatoire des équipes
        Collections.shuffle(players);

        // Ajout des équipes
        this.teams = Arrays.asList(
                new Team(players.subList(0, NB_PLAYERS_TEAMS)),
                new Team(players.subList(NB_PLAYERS_TEAMS, NB_PLAYERS))
        );
    }

    /**
     * Démarrer la partie
     */
    public void startGame() {
        // A implémenter. Ici, juste envoi d'une carte de test
        for (Player player : players)
            player.distributeCard(new Card(CardType.JACK, CardColor.HEART));
    }
}
