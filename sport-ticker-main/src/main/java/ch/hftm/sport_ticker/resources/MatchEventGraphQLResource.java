package ch.hftm.sport_ticker.resources;

import java.util.List;

import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import ch.hftm.sport_ticker.model.EventType;
import ch.hftm.sport_ticker.model.GameMatch;
import ch.hftm.sport_ticker.model.MatchEvent;
import ch.hftm.sport_ticker.model.Player;
import ch.hftm.sport_ticker.services.GameMatchService;
import ch.hftm.sport_ticker.services.MatchEventService;
import ch.hftm.sport_ticker.services.PlayerService;
import io.smallrye.graphql.api.Subscription;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@GraphQLApi
@ApplicationScoped
public class MatchEventGraphQLResource {

    @Inject
    MatchEventService matchEventService;

    @Inject
    GameMatchService matchService;

    @Inject
    PlayerService playerService;

    BroadcastProcessor<MatchEvent> processor = BroadcastProcessor.create();

    // Subscription

    @Subscription("eventAdded")
    @Description("Subscribe to new match events for a specific match")
    public Multi<MatchEvent> eventAdded(@Name("matchId") Long matchId) {
        return processor
            .filter(event -> event.match != null && event.match.id.equals(matchId));
    }

    // --- QUERIES ---

    @Query("eventsByMatch")
    @Description("Get all events for a match")
    public List<MatchEvent> getEventsByMatch(@Name("matchId") Long matchId) {
        GameMatch match = matchService.findById(matchId);
        if (match == null) throw new RuntimeException("Match not found");
        return matchEventService.getEventsForMatch(match);
    }

    @Query("event")
    @Description("Get a match event by ID")
    public MatchEvent getEvent(@Name("id") Long id) {
        return matchEventService.findById(id);
    }

    // --- MUTATIONS ---

    @Mutation("addMatchEvent")
    @Description("Add a new event to a match")
    public MatchEvent addMatchEvent(@Name("matchId") Long matchId,
                                    @Name("type") EventType type,
                                    @Name("playerId") Long playerId) {

        GameMatch match = matchService.findById(matchId);
        if (match == null) throw new RuntimeException("Match not found");

        Player player = null;
        if (playerId != null) {
            player = playerService.findById(playerId);
            if (player == null) throw new RuntimeException("Player not found");
        }

        MatchEvent newEvent = matchEventService.addEvent(match, type, player);

        // Propagate newly created event to BroadcastProcessor for active subscriptions
        processor.onNext(newEvent);

        // Emit kafka event for other internal services listening
        matchEventService.emitMatchEvent(newEvent);
        
        return newEvent;
    }

    @Mutation("deleteMatchEvent")
    @Description("Delete a match event by ID")
    public Boolean deleteMatchEvent(@Name("id") Long id) {
        matchEventService.deleteEvent(id);
        return true;
    }
}
