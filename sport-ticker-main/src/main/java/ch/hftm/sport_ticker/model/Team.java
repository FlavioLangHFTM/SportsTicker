package ch.hftm.sport_ticker.model;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Team extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Player> players;

    public static Team findByName(String name) {
        return find("name", name).firstResult();
    }
}
