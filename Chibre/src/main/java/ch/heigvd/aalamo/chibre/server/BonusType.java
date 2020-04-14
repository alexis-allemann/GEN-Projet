package ch.heigvd.aalamo.chibre.server;

public enum BonusType {
    SCHTOCKR(20),
    THREE_CARDS(20),
    FOUR_CARDS(50),
    SQUARE_CARDS(100),
    SUITE(100),
    SQUARE_NINE(150),
    SQUARE_JACKS(200);

    private int points;

    BonusType(int points){
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
