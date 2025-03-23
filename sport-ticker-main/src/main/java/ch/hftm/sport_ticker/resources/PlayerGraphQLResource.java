package ch.hftm.sport_ticker.resources;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import ch.hftm.sport_ticker.model.Player;
import ch.hftm.sport_ticker.model.Team;
import ch.hftm.sport_ticker.services.PlayerService;
import ch.hftm.sport_ticker.services.TeamService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@GraphQLApi
@ApplicationScoped
public class PlayerGraphQLResource {

    @Inject
    PlayerService playerService;

    @Inject
    TeamService teamService;

    @Query("players")
    @Description("Get all players")
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @Query("player")
    @Description("Get a player by Id")
    public Player getPlayer(@Name("id") Long id) {
        return playerService.findById(id);
    }

    @Query("playersByTeam")
    @Description("Get all players for a specific team")
    public List<Player> getPlayersByTeam(@Name("teamId") Long teamId) {
        Team team = teamService.findById(teamId);
        return team != null ? playerService.findByTeam(team) : null;
    }

    @Mutation("createPlayer")
    @Description("Create a new player for a team")
    public Player createPlayer(@Name("name") String name,
                               @Name("teamId") Long teamId) {
        Team team = teamService.findById(teamId);
        if (team == null) {
            throw new RuntimeException("Team not found");
        }
        return playerService.createPlayer(name, team);
    }

    @Mutation("deletePlayer")
    @Description("Delete a player by Id")
    public Boolean deletePlayer(@Name("id") Long id) {
        playerService.deletePlayer(id);
        return true;
    }
}
