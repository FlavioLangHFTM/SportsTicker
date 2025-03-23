package ch.hftm.sport_ticker.resources;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import ch.hftm.sport_ticker.model.Team;
import ch.hftm.sport_ticker.services.TeamService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@GraphQLApi
@ApplicationScoped
public class TeamGraphQLResource {

    @Inject
    TeamService teamService;

    // --- QUERIES ---

    @Query("teams")
    @Description("Get all teams")
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @Query("team")
    @Description("Get a team by Id")
    public Team getTeamById(@Name("id") Long id) {
        return teamService.findById(id);
    }

    // --- MUTATIONS ---

    @Mutation("createTeam")
    @Description("Create a new team with a unique name")
    public Team createTeam(@Name("name") String name) {
        return teamService.createTeam(name);
    }

    @Mutation("addPlayerToTeam")
    @Description("Add a player to a team using name or Id")
    public Team addPlayerToTeam(@Name("teamId") Long teamId,
            @Name("playerName") String playerName,
            @Name("playerId") Long playerId) {
        return teamService.addPlayerToTeam(teamId, playerName, playerId);
    }

    @Mutation("removePlayerFromTeam")
    @Description("Remove a player from a team using name or Id")
    public Team removePlayerFromTeam(@Name("teamId") Long teamId,
            @Name("playerName") String playerName,
            @Name("playerId") Long playerId) {
        return teamService.removePlayerFromTeam(teamId, playerName, playerId);
    }

    @Mutation("deleteTeam")
    @Description("Delete a team by Id")
    public Boolean deleteTeam(@Name("id") Long id) {
        teamService.deleteTeam(id);
        return true;
    }
}
