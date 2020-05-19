package ch.heigvd.aalamo.chibre.network.objects.DTOs;


import ch.heigvd.aalamo.chibre.engine.BonusType;

public class AnnouncementDTO implements DTO {
    private int id;
    private BonusType bonusType;
    private CardDTO card;

    public AnnouncementDTO(int id, BonusType bonusType) {
        this(id, bonusType, null);
    }

    public AnnouncementDTO(int id, BonusType bonusType, CardDTO card) {
        this.id = id;
        this.bonusType = bonusType;
        this.card = card;
    }

    public int getId() {
        return id;
    }

    public BonusType getBonusType() {
        return bonusType;
    }

    public CardDTO getCard() {
        return card;
    }
}
