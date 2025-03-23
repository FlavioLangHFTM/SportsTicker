package ch.hftm.sport_ticker.services;

import java.util.List;

import ch.hftm.sport_ticker.model.GameMatch;
import ch.hftm.sport_ticker.model.MatchStatus;
import ch.hftm.sport_ticker.model.Team;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GameMatchService {

    public List<GameMatch> getAllMatches() {
        return GameMatch.listAll();
    }

    public GameMatch findById(Long id) {
        return GameMatch.findById(id);
    }

    @Transactional
    public GameMatch createMatch(Team teamA, Team teamB) {
        GameMatch match = new GameMatch();
        match.teamA = teamA;
        match.teamB = teamB;
        match.status = MatchStatus.NOT_STARTED;
        match.persist();
        return match;
    }

    @Transactional
    public GameMatch updateScore(Long matchId, int scoreA, int scoreB) {
        GameMatch match = GameMatch.findById(matchId);
        if (match == null) {
            throw new IllegalArgumentException("Match not found");
        }
        match.scoreA = scoreA;
        match.scoreB = scoreB;
        match.persist();
        return match;
    }

    @Transactional
    public GameMatch updateStatus(Long matchId, MatchStatus newStatus) {
        GameMatch match = GameMatch.findById(matchId);
        if (match == null) {
            throw new IllegalArgumentException("Match not found");
        }
        match.status = newStatus;
        match.persist();
        return match;
    }

    @Transactional
    public void deleteMatch(Long matchId) {
        GameMatch match = GameMatch.findById(matchId);
        if (match != null) {
            match.delete();
        }
    }
}
