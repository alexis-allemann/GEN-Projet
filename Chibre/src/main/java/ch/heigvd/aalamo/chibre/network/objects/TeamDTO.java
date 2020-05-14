package ch.heigvd.aalamo.chibre.network.objects;

import java.util.List;

public class TeamDTO implements DTO {
    private int points;
    private List<PlayerDTO> players;
    private int id;

    public TeamDTO(int id, int points, List<PlayerDTO> players) {
        this.points = points;
        this.players = players;
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public int getId() {
        return id;
    }
}
