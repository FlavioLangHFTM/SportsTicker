package ch.hftm.sport_ticker.services;

import java.util.List;

import ch.hftm.sport_ticker.model.Player;
import ch.hftm.sport_ticker.model.Team;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TeamService {

    public List<Team> getAllTeams() {
        return Team.listAll();
    }

    public Team findById(Long teamId) {
        return Team.findById(teamId);
    }

    public Team findByName(String name) {
        return Team.find("name", name).firstResult();
    }

    @Transactional
    public Team createTeam(String name) {
        Team existing = findByName(name);
        if (existing != null) {
            throw new RuntimeException("A team with that name already exists.");
        }

        Team team = new Team();
        team.name = name;
        team.persist();
        return team;
    }

    @Transactional
    public Team createTeamWithPlayers(String teamName, List<String> playerNames) {

        Team team = new Team();
        team.name = teamName;
        team.persist();

        for (String playerName : playerNames) {
            Player player = new Player();
            player.name = playerName;
            player.team = team;
            player.persist();
        }

        return team;
    }

    @Transactional
    public Team addPlayerToTeam(Long teamId, String playerName, Long playerId) {
        Team team = findById(teamId);
        if (team == null)
            throw new RuntimeException("Team not found");

        Player player;

        if (playerId != null) {
            player = Player.findById(playerId);
            if (player == null)
                throw new RuntimeException("Player not found");
            player.team = team;
            player.persist();
        } else if (playerName != null && !playerName.isBlank()) {
            player = new Player();
            player.name = playerName;
            player.team = team;
            player.persist();
        } else {
            throw new RuntimeException("Either playerName or playerId must be provided");
        }

        return team;
    }

    @Transactional
    public Team removePlayerFromTeam(Long teamId, String playerName, Long playerId) {
        Team team = findById(teamId);
        if (team == null)
            throw new RuntimeException("Team not found");

        Player player = null;

        if (playerId != null) {
            player = Player.findById(playerId);
        } else if (playerName != null && !playerName.isBlank()) {
            player = Player.find("name = ?1 and team = ?2", playerName, team).firstResult();
        }

        if (player == null || !player.team.equals(team)) {
            throw new RuntimeException("Player not found in this team");
        }

        player.delete();
        return team;
    }

    @Transactional
    public void deleteTeam(Long playerId) {
        Team team = findById(playerId);
        if (team != null) {
            team.delete();
        }
    }
}
