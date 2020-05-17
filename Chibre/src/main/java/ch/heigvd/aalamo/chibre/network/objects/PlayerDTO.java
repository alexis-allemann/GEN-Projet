package ch.heigvd.aalamo.chibre.network.objects;

import java.util.List;
import java.util.Objects;

public class PlayerDTO implements DTO {
    private String username;
    private List<CardDTO> cards;

    public PlayerDTO(String username, List<CardDTO> cards) {
        this.username = username;
        this.cards = cards;
    }

    public String getUsername() {
        return username;
    }

    public List<CardDTO> getCards() {
        return cards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDTO playerDTO = (PlayerDTO) o;
        return Objects.equals(username, playerDTO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
