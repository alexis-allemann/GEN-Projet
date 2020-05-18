package ch.heigvd.aalamo.chibre.network.objects.DTOs;


import ch.heigvd.aalamo.chibre.engine.BonusType;

public class AnnouncementDTO implements DTO{
    private int id;
    private PlayerDTO player;
    private BonusType bonusType;
    private CardDTO card;

    public AnnouncementDTO(int id, PlayerDTO player, BonusType bonusType, CardDTO card) {
        this.id = id;
        this.player = player;
        this.bonusType = bonusType;
        this.card = card;
    }

    public int getId() {
        return id;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public BonusType getBonusType() {
        return bonusType;
    }

    public CardDTO getCard() {
        return card;
    }
}
