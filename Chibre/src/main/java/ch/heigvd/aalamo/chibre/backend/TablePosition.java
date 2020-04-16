package ch.heigvd.aalamo.chibre.backend;

public enum TablePosition {
    TOP(0),
    RIGHT(1),
    BOTTOM(0),
    LEFT(1);

    private int team;

    TablePosition(int team){
        this.team = team;
    }

    public int getTeam() {
        return team;
    }
}
