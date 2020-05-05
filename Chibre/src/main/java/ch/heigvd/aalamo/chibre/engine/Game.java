/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Game.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une partie de Chibre
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import java.util.*;

public class Game implements Runnable {
    // Constantes globles du jeu
    public static final int NB_TEAMS = 2;
    public static final int NB_PLAYERS_TEAMS = 2;
    public static final int NB_CARDS = 36;
    public static final int WIN_POINTS = 1000;
    public static final int NB_PLAYERS = NB_TEAMS * NB_PLAYERS_TEAMS;
    public static final int NB_CARDS_PLAYER = NB_CARDS / NB_PLAYERS;

    // Attributs
    private final int id;
    private static int count = 1;
    private List<Player> players;
    private final Table table;
    private List<Team> teams = new ArrayList<>(NB_TEAMS);
    private List<Round> rounds = new ArrayList<>();
    Player firstPlayerTrump;

    /**
     * Instancier une parte
     *
     * @param players liste des joueurs
     */
    public Game(List<Player> players) {
        if (players == null || players.contains(null))
            throw new IllegalArgumentException("Liste de joueurs illégale (nulle ou joueur nul)");

        for (Player player : players)
            player.setGame(this);

        this.id = count++;
        this.players = players;
        setTeams(players);
        this.table = new Table(this);
    }

    // Getters

    /**
     * @return l'id de la partie
     */
    public int getId() {
        return id;
    }

    /**
     * @return la liste de joueurs de la partie
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return les équipes de la partie
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * @return le tour actuellement joué, null si aucun tour n'est joué
     */
    public Round getCurrentRound() {
        Round round = null;
        for (Round r : rounds)
            if (r.isPlayed())
                round = r;

        return round;
    }

    /**
     * @return la liste des tours de la partie
     */
    public List<Round> getRounds() {
        return rounds;
    }

    /**
     * @return la table de jeu de la partie
     */
    public Table getTable() {
        return table;
    }

    /**
     * @return le premier joueur qui doit faire atout
     */
    public Player getFirstPlayerTrump() {
        return firstPlayerTrump;
    }

    // Setters

    /**
     * Définir le joueur qui fait atout en premier
     *
     * @param firstPlayerTrump joueur
     */
    public void setFirstPlayerTrump(Player firstPlayerTrump) {
        this.firstPlayerTrump = firstPlayerTrump;
    }

    // Méthodes

    /**
     * Démarrage du thread de la partie et instanciation du premier tour
     */
    @Override
    public void run() {
        Round round = new Round(this, true);
        rounds.add(round);
        round.initRound();
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
     * Instancier un nouveau tour de jeu
     */
    public void newRound() {
        if (teams.get(0).getPoints() < WIN_POINTS && teams.get(1).getPoints() < WIN_POINTS) {
            Round round = new Round(this, true);
            rounds.add(round);
            round.initRound();
        }
    }
}
