package ch.hftm.sport_ticker.services;

import java.util.List;

import ch.hftm.sport_ticker.model.Player;
import ch.hftm.sport_ticker.model.Team;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PlayerService {

    public List<Player> getAllPlayers() {
        return Player.listAll();
    }

    public Player findById(Long playerId) {
        return Player.findById(playerId);
    }

    public List<Player> findByTeam(Team team) {
        return Player.findByTeam(team);
    }

    @Transactional
    public Player createPlayer(String name, Team team) {
        Player player = new Player();
        player.name = name;
        player.team = team;
        player.persist();
        return player;
    }

    @Transactional
    public void deletePlayer(Long playerId) {
        Player player = findById(playerId);
        if (player != null) {
            player.delete();
        }
    }
}
