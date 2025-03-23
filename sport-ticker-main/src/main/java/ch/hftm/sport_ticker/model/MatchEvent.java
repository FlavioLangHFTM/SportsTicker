package ch.hftm.sport_ticker.model;

import java.time.LocalDateTime;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class MatchEvent extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    public GameMatch match;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public EventType type;

    @ManyToOne
    @JoinColumn(name = "player_id")
    public Player player; // Spieler, der das Event ausgelöst hat (optional)

    @Column(nullable = false)
    public LocalDateTime timestamp = LocalDateTime.now();

    public static List<MatchEvent> findByMatch(GameMatch match) {
        return list("match", match);
    }
}
