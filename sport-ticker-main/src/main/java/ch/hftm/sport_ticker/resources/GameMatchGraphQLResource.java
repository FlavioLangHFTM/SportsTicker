package ch.hftm.sport_ticker.resources;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import ch.hftm.sport_ticker.model.GameMatch;
import ch.hftm.sport_ticker.model.MatchStatus;
import ch.hftm.sport_ticker.model.Team;
import ch.hftm.sport_ticker.services.GameMatchService;
import ch.hftm.sport_ticker.services.TeamService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@GraphQLApi
@ApplicationScoped
public class GameMatchGraphQLResource {

    @Inject
    GameMatchService matchService;

    @Inject
    TeamService teamService;

    // Queries

    @Query("matches")
    @Description("Get all matches")
    public List<GameMatch> getAllMatches() {
        return matchService.getAllMatches();
    }

    @Query("match")
    @Description("Get a match by Id")
    public GameMatch getMatchById(@Name("id") Long id) {
        return matchService.findById(id);
    }

    // Mutations

    @Mutation("createMatch")
    @Description("Create a new match with two team Ids")
    public GameMatch createMatch(@Name("teamAId") Long teamAId,
                                 @Name("teamBId") Long teamBId) {
        Team teamA = teamService.findById(teamAId);
        Team teamB = teamService.findById(teamBId);

        if (teamA == null || teamB == null) {
            throw new RuntimeException("One or both teams not found.");
        }

        return matchService.createMatch(teamA, teamB);
    }

    @Mutation("updateMatchScore")
    @Description("Update the score of a match")
    public GameMatch updateMatchScore(@Name("matchId") Long matchId,
                                      @Name("scoreA") int scoreA,
                                      @Name("scoreB") int scoreB) {
        return matchService.updateScore(matchId, scoreA, scoreB);
    }

    @Mutation("updateMatchStatus")
    @Description("Update the status of a match")
    public GameMatch updateMatchStatus(@Name("matchId") Long matchId,
                                       @Name("status") MatchStatus status) {
        return matchService.updateStatus(matchId, status);
    }

    @Mutation("deleteMatch")
    @Description("Delete a match by Id")
    public Boolean deleteMatch(@Name("matchId") Long matchId) {
        matchService.deleteMatch(matchId);
        return true;
    }
}
