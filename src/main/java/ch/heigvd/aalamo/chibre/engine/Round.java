/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Round.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un tour d'atout dans une partie
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.AnnouncementDTO;
import ch.heigvd.aalamo.chibre.network.objects.Request;
import ch.heigvd.aalamo.chibre.network.objects.ServerAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Round {
    // Attributs
    private final int id;
    private static int count = 1;
    private CardColor trumpColor;
    private final List<Turn> turns = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private List<Announcement> possibleAnnouncements = new ArrayList<>();
    private List<Announcement> announcements = new ArrayList<>();
    private final CardCollection cardCollection = new CardCollection();
    private final Game game;
    private boolean isPlayed;
    private Player trumpPlayer;

    /**
     * Instancier un tour de jeu
     */
    public Round(Game game) {
        this.game = game;
        this.id = count++;
        this.isPlayed = true;
    }

    // Getters

    /**
     * @return le joueur qui fait atout dans le tour
     */
    public Player getTrumpPlayer() {
        return trumpPlayer;
    }

    /**
     * @return la partie qui a instancier le tour
     */
    public Game getGame() {
        return game;
    }

    /**
     * @return la couleur atout du tour
     */
    public CardColor getTrumpColor() {
        return trumpColor;
    }

    /**
     * @return la table de jeu du tour
     */
    public Table getTable() {
        return game.getTable();
    }

    /**
     * @return la tour de table en cours, null si aucun tour de table n'a commencé
     */
    public Turn getCurrentTurn() {
        for (Turn turn : turns)
            if (turn.isPlayed())
                return turn;

        return null;
    }

    /**
     * @return l'id du round
     */
    public int getId() {
        return id;
    }

    public List<Turn> getTurns() {
        return turns;
    }


    public List<Player> getPlayers(){
        return game.getPlayers();
    }

    // Setters

    /**
     * Définir la couleur atout du tour
     *
     * @param trumpColor la couleur atout
     */
    public void setTrumpColor(CardColor trumpColor) {
        this.trumpColor = trumpColor;
        game.sendToAllPlayers(new Request(ServerAction.SEND_TRUMP_COLOR, trumpColor));
    }

    // Méthodes

    /**
     * Initialiser le tour
     */
    public void initRound() {
        System.out.println("Début du round id#" + id);

        for (Player player : game.getPlayers()) {
            if (cardCollection.distributeCards(player, Game.NB_CARDS_PLAYER) && game.getRounds().size() == 1)
                game.setFirstPlayerTrump(player);
        }

        this.trumpPlayer = game.getTable().nextTrumpPlayer(id, game.getTable().getPositionByPlayer(game.getFirstPlayerTrump()));

        game.sendToAllPlayers(new Request(ServerAction.SEND_TRUMP_PLAYER, trumpPlayer.serialize()));

        trumpPlayer.sendRequest(new Request(ServerAction.ASK_TRUMP));
    }

    /**
     * Savoir si le tour en train d'être joué
     *
     * @return si le tour en train d'être joué
     */
    public boolean isPlayed() {
        return isPlayed;
    }

    /**
     * Démarrer un tour de table
     */
    public void initTurn() {
        Turn turn = new Turn(this, null);
        turns.add(turn);
        turn.initTurn();
    }

    /**
     * Savoir si le tour de table est le premier du tour
     *
     * @return booléen si c'est le premier ou non
     */
    public boolean isFirstTurn() {
        return turns.size() == 1;
    }

    /**
     * Ajouter un tour de table au tour de jeu
     *
     * @param turn tour de table à ajouter
     */
    public void addTurn(Turn turn) {
        turns.add(turn);
    }

    /**
     * Identification des annonces des joueurs et envoi de celle-ci
     */
    public void initAnnoucement() {
        for (Player player : game.getPlayers())
            player.sendAnnoucements(trumpColor);
    }

    /**
     * Retrouver un annonce à partir de son identifiant
     *
     * @param id l'identifiant de l'annonce
     * @return l'annonce correspondant à l'id donné
     */
    public Announcement getAnnouncementByID(int id) {
        for (Announcement announcement : possibleAnnouncements)
            if (announcement.getId() == id)
                return announcement;

        return null;
    }

    /**
     * Ajout d'une annonce jouée par l'utilisateur
     *
     * @param announcement l'annonce
     */
    public void addAnnouncement(Announcement announcement) {
        announcements.add(announcement);
        if (announcement.getBonusType() != BonusType.SCHTOCKR) {
            System.out.println(announcement.getPlayer() + "annonce " + announcement.getBonusType().toString());
            game.sendToAllPlayers(new Request(ServerAction.DISPLAY_ANNOUNCEMENT, announcement.serialize()));
        }
    }

    /**
     * Ajout d'une annonce possible d'être jouée par un utilisateur
     *
     * @param announcement l'annonce
     */
    public void addPossibleAnnouncement(Announcement announcement) {
        possibleAnnouncements.add(announcement);
    }

    /**
     * Ajout des points des annonces
     */
    public void givePointsForAnnouncements() {
        // Détection de la meilleure annonce
        Announcement bestAnnouncement = null;
        for (Announcement announcement : announcements) {
            if (announcement.getBonusType() != BonusType.SCHTOCKR) {
                if (
                        bestAnnouncement == null ||

                                // Comparaison des points de l'annonce
                                bestAnnouncement.getPoints() > announcement.getPoints() ||

                                // Comparaison du nombre de carte à la suite
                                (bestAnnouncement.getPoints() == announcement.getPoints() &&
                                        announcement.getNbSuiteCards() > bestAnnouncement.getNbSuiteCards()) ||

                                // Comparaison de la hauteur de la plus haute carte
                                (bestAnnouncement.getPoints() == announcement.getPoints() &&
                                        announcement.getOrder() > bestAnnouncement.getOrder()) ||

                                // Comparaison de la couleur avec l'atout
                                (bestAnnouncement.getPoints() == announcement.getPoints() &&
                                        announcement.getOrder() == bestAnnouncement.getOrder() &&
                                        announcement.getCardColor() == trumpColor &&
                                        bestAnnouncement.getCardColor() != trumpColor)
                )
                    bestAnnouncement = announcement;
            }
        }

        // S'il y a une annonce
        if (bestAnnouncement != null) {
            // Ajout de toutes les annonces de l'équipe ayant la meilleure annonce
            for (Announcement announcement : announcements)
                // On n'ajoute pas le Schötckr (ajouté lorsque la seconde carte est posée)
                if (announcement.getBonusType() != BonusType.SCHTOCKR)
                    if (announcement.getTeam() == bestAnnouncement.getTeam())
                        announcement.getTeam().addPoints(announcement.getPoints());
        }

        sendPoints();
    }

    /**
     * Envoi des points aux équipes
     */
    void sendPoints() {
        // Envoi des équipes pour mise à jour des points sur la GUI
        game.sendToAllPlayers(new Request(ServerAction.SEND_POINTS, new ArrayList<>(Arrays.asList(
                game.getTeams().get(0).serialize(),
                game.getTeams().get(1).serialize()
        ))));
    }
}
