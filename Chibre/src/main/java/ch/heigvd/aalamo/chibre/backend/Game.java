/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Game.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une partie de Chibre
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.backend;



import jdk.internal.net.http.common.Pair;

import java.util.*;

public class Game {
    // Constantes globles du jeu
    public static final int NB_TEAMS = 2;
    public static final int NB_PLAYERS_TEAMS = 2;
    public static final int NB_CARDS = 36;
    public static final int NB_CARDS_PLAYER = 9;
    public static final int NB_PLAYERS = NB_TEAMS * NB_PLAYERS_TEAMS;
    public static final int WIN_POINTS = 1000;
    public static final boolean CLOCKWISE = true;

    // Attributs
    private int id;
    private Random random = new Random();
    private List<Player> players;
    private List<Pair<Player, TablePosition>> table;
    private List<Team> teams = new ArrayList<>(NB_TEAMS);
    private List<Round> rounds = new ArrayList<>();
    private CardCollection cardCollection = new CardCollection();

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
        setTable(teams);
    }

    private void setTable(List<Team> teams) {
        int teamPosition = random.nextInt(1);
        for(Team team : teams){
            int playerPosition = random.nextInt(team.getMaxIDPlayer() - team.getMinIDPlayer()) +
                    team.getMinIDPlayer();
            if(TablePosition.TOP.getTeam() == teamPosition){
                if(playerPosition == team.getPlayers().get(0).getId()){
                    table.add(new Pair<>(team.getPlayers().get(0),TablePosition.TOP));
                    table.add(new Pair<>(team.getPlayers().get(1),TablePosition.BOTTOM));
                }
                else{
                    table.add(new Pair<>(team.getPlayers().get(1),TablePosition.TOP));
                    table.add(new Pair<>(team.getPlayers().get(0),TablePosition.BOTTOM));
                }
            }
            else{
                if(playerPosition == team.getPlayers().get(0).getId()){
                    table.add(new Pair<>(team.getPlayers().get(0),TablePosition.RIGHT));
                    table.add(new Pair<>(team.getPlayers().get(1),TablePosition.LEFT));
                }
                else{
                    table.add(new Pair<>(team.getPlayers().get(1),TablePosition.RIGHT));
                    table.add(new Pair<>(team.getPlayers().get(0),TablePosition.LEFT));
                }
            }
        }
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
        // TODO : niveau conception, il faudrait dans le start game, créer un nouveau round au lieu de distribuer
        //  les cartes et les distribuer dans le round (UML à modifier)
        for (Player player : players)
            cardCollection.distributeCards(player, NB_CARDS_PLAYER);
    }
}
