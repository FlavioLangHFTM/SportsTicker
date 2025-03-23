package ch.hftm.sport_ticker.model;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class GameMatch extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "teamA_id", nullable = false)
    public Team teamA;

    @ManyToOne
    @JoinColumn(name = "teamB_id", nullable = false)
    public Team teamB;

    @Column(nullable = false)
    public int scoreA = 0;

    @Column(nullable = false)
    public int scoreB = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public MatchStatus status = MatchStatus.NOT_STARTED;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<MatchEvent> events;
}
